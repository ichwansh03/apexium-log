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
    private val traceJobService: TraceJobService,
    private val logRepository: LogRepository
) {
    private val salesforceIdRegex = Regex("^[a-zA-Z0-9]{15}(?:[a-zA-Z0-9]{3})?$")

    private fun isValidSalesforceId(id: String): Boolean = salesforceIdRegex.matches(id)
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

    @GetMapping("/{id}/download")
    fun downloadLog(
        @PathVariable id: String,
        @RequestParam(required = false) operation: String?
    ): ResponseEntity<org.springframework.core.io.Resource> {
        val stream = logService.getLogDownloadStream(id)
        return if (stream != null) {
            val downloadName = "${operation ?: "log"}_$id.log.gz"
            val resource = org.springframework.core.io.InputStreamResource(stream)
            ResponseEntity.ok()
                .contentType(org.springframework.http.MediaType.parseMediaType("application/gzip"))
                .header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"$downloadName\"")
                .body(resource)
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
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
        if (!isValidSalesforceId(id)) {
            return ResponseEntity.badRequest().build()
        }
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
