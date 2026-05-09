package com.observability.sfdc.service

import com.observability.sfdc.domain.Log
import com.observability.sfdc.repository.LogRepository
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@Service
class SalesforceLogPollingService(
    private val logService: SalesforceLogService,
    private val logRepository: LogRepository
) {
    private val logger = LoggerFactory.getLogger(SalesforceLogPollingService::class.java)
    private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ")

    @Scheduled(fixedRate = 60000)
    fun pollLogs() {
        logger.info("Polling Salesforce logs...")
        val logs = logService.queryApexLogs(limit = 20)
        
        var newLogsCount = 0
        logs.forEach { dto ->
            if (!logRepository.findBySfdcId(dto.id).isPresent) {
                val log = Log(
                    sfdcId = dto.id,
                    apexClassName = dto.apexClassName,
                    authorName = dto.logUser?.name,
                    requestTime = parseDateTime(dto.startTime),
                    operation = dto.operation,
                    logSize = dto.logLength,
                    duration = dto.durationMilliseconds,
                    status = dto.status,
                    request = dto.request
                )
                logRepository.save(log)
                newLogsCount++
            }
        }
        
        if (newLogsCount > 0) {
            logger.info("Saved $newLogsCount new logs to the database.")
        } else {
            logger.info("No new logs found.")
        }
    }

    private fun parseDateTime(startTime: String?): LocalDateTime? {
        if (startTime == null) return null
        return try {
            // Salesforce format: 2026-05-09T10:00:00.000+0000
            OffsetDateTime.parse(startTime, dateTimeFormatter).toLocalDateTime()
        } catch (e: Exception) {
            logger.warn("Failed to parse start time: $startTime. Error: ${e.message}")
            null
        }
    }
}
