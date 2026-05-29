package com.observability.sfdc.service

import com.observability.sfdc.domain.TraceJob
import com.observability.sfdc.dto.FrontendTraceFlagRequest
import com.observability.sfdc.repository.TraceJobRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Service
class TraceJobService(
    private val traceJobRepository: TraceJobRepository,
    private val logService: SalesforceLogService
) {
    private val logger = LoggerFactory.getLogger(TraceJobService::class.java)
    private val sfdcFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")

    @Transactional
    fun createJob(request: FrontendTraceFlagRequest): TraceJob {
        val totalMinutes = request.getTotalMinutes()
        if (totalMinutes <= 0) {
            throw IllegalArgumentException("Total duration must be at least 1 minute")
        }

        val startTime = LocalDateTime.now()
        val endTime = startTime.plusDays((request.durationDays ?: 0).toLong())
            .plusHours((request.durationHours ?: 0).toLong())
            .plusMinutes((request.durationMinutes ?: 0).toLong())
        
        val job = TraceJob(
            tracedEntityId = request.tracedEntityId,
            tracedEntityName = null, // Optional: resolve from SF or pass from FE
            tracedEntityType = request.entityType ?: "User",
            debugLevelName = request.debugLevelName,
            startTime = startTime,
            endTime = endTime,
            status = "ACTIVE"
        )
        
        val savedJob = traceJobRepository.save(job)
        
        // Trigger initial TraceFlag in Salesforce
        refreshSalesforceTraceFlag(savedJob)
        
        return savedJob
    }

    fun getAllJobs(): List<TraceJob> = traceJobRepository.findAll()

    @Transactional
    fun cancelJob(id: Long) {
        val job = traceJobRepository.findById(id).orElseThrow { RuntimeException("Job not found") }
        job.status = "CANCELLED"
        job.sfdcTraceFlagId?.let { 
            logService.deleteTraceFlag(it)
        }
        traceJobRepository.save(job)
    }

    @Transactional
    fun refreshSalesforceTraceFlag(job: TraceJob) {
        val now = ZonedDateTime.now(ZoneId.of("UTC"))
        val jobEndTime = job.endTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC"))
        
        // Salesforce Limit: Expiration must be within 24h of StartDate (or current time if updating)
        // We set it to 23 hours to have a safety buffer for the next sliding window cycle
        val maxSafeExpiry = now.plusHours(23)
        val targetExpiry = if (jobEndTime.isBefore(maxSafeExpiry)) jobEndTime else maxSafeExpiry
        
        if (job.sfdcTraceFlagId == null) {
            // Create NEW
            val duration = java.time.Duration.between(now, targetExpiry).toMinutes().coerceAtLeast(1).toInt()
            val response = logService.createTraceFlag(
                FrontendTraceFlagRequest(
                    tracedEntityId = job.tracedEntityId,
                    debugLevelName = job.debugLevelName,
                    durationMinutes = duration,
                    entityType = job.tracedEntityType
                )
            )
            if (response?.success == true) {
                job.sfdcTraceFlagId = response.id
                traceJobRepository.save(job)
                logger.info("Job ${job.id}: Created initial Salesforce TraceFlag ${response.id} expiring at ${targetExpiry.format(sfdcFormatter)}")
            } else {
                logger.error("Job ${job.id}: Failed to create Salesforce TraceFlag: ${response?.errors}")
            }
        } else {
            // Sliding Window PATCH
            val success = logService.patchTraceFlag(job.sfdcTraceFlagId!!, targetExpiry.format(sfdcFormatter))
            if (success) {
                logger.info("Job ${job.id}: Slid window for TraceFlag ${job.sfdcTraceFlagId} to ${targetExpiry.format(sfdcFormatter)}")
            } else {
                logger.warn("Job ${job.id}: Failed to patch TraceFlag ${job.sfdcTraceFlagId}. Will attempt re-creation.")
                job.sfdcTraceFlagId = null
                traceJobRepository.save(job)
                // Recursive call to re-create
                refreshSalesforceTraceFlag(job)
            }
        }
    }
}
