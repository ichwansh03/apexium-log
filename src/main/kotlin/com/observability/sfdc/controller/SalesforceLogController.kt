package com.observability.sfdc.controller

import com.observability.sfdc.domain.Log
import com.observability.sfdc.domain.TraceJob
import com.observability.sfdc.dto.*
import com.observability.sfdc.repository.LogRepository
import com.observability.sfdc.service.SalesforceLogService
import com.observability.sfdc.service.TraceJobService
import jakarta.validation.Valid
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/sfdc/logs")
class SalesforceLogController(
    private val logService: SalesforceLogService,
    private val logRepository: LogRepository,
    private val traceJobService: TraceJobService
) {

    @GetMapping
    fun getApexLogs(
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(defaultValue = "0") page: Int
    ): List<ApexLogDto> {
        val offset = page * size
        return logService.queryApexLogs(size, offset)
    }

    @GetMapping("/db")
    fun getDbLogs(
        @RequestParam(required = false) className: String?,
        @RequestParam(required = false) author: String?,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(defaultValue = "0") page: Int
    ): List<Log> {
        val pageable = PageRequest.of(page, size, Sort.by("requestTime").descending())
        return when {
            !className.isNullOrBlank() && !author.isNullOrBlank() ->
                logRepository.findByApexClassNameContainingIgnoreCaseAndAuthorNameContainingIgnoreCase(className, author, pageable)
            !className.isNullOrBlank() ->
                logRepository.findByApexClassNameContainingIgnoreCase(className, pageable)
            !author.isNullOrBlank() ->
                logRepository.findByAuthorNameContainingIgnoreCase(author, pageable)
            else -> logRepository.findAllByOrderByRequestTimeDesc(pageable)
        }
    }

    @GetMapping("/{id}/body")
    fun getLogBody(@PathVariable id: String): String? {
        return logService.getLogBody(id)
    }

    @PostMapping("/trace-flags")
    fun createTraceFlag(@Valid @RequestBody request: FrontendTraceFlagRequest): SalesforceCreateResponse? {
        return logService.createTraceFlag(request)
    }

    @GetMapping("/trace-flags")
    fun getActiveTraceFlags(): List<TraceFlagDto> {
        return logService.getActiveTraceFlags()
    }

    @DeleteMapping("/trace-flags/{id}")
    fun deleteTraceFlag(@PathVariable id: String): ResponseEntity<Unit> {
        val deleted = logService.deleteTraceFlag(id)
        return if (deleted) ResponseEntity.ok().build() else ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
    }

    // --- Trace Job Endpoints ---

    @PostMapping("/trace-jobs")
    fun createTraceJob(@Valid @RequestBody request: FrontendTraceFlagRequest): TraceJob {
        return traceJobService.createJob(request)
    }

    @GetMapping("/trace-jobs")
    fun getTraceJobs(): List<TraceJob> {
        return traceJobService.getAllJobs()
    }

    @DeleteMapping("/trace-jobs/{id}")
    fun cancelTraceJob(@PathVariable id: Long): ResponseEntity<Unit> {
        traceJobService.cancelJob(id)
        return ResponseEntity.ok().build()
    }
}
