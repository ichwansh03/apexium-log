package com.observability.sfdc.controller

import com.observability.sfdc.domain.Log
import com.observability.sfdc.dto.ApexLogDto
import com.observability.sfdc.dto.SalesforceCreateResponse
import com.observability.sfdc.dto.TraceFlagRequest
import com.observability.sfdc.repository.LogRepository
import com.observability.sfdc.service.SalesforceLogService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/sfdc/logs")
class SalesforceLogController(
    private val logService: SalesforceLogService,
    private val logRepository: LogRepository
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
        @RequestParam(required = false) author: String?
    ): List<Log> {
        return when {
            !className.isNullOrBlank() && !author.isNullOrBlank() ->
                logRepository.findByApexClassNameContainingIgnoreCaseAndAuthorNameContainingIgnoreCase(className, author)
            !className.isNullOrBlank() ->
                logRepository.findByApexClassNameContainingIgnoreCase(className)
            !author.isNullOrBlank() ->
                logRepository.findByAuthorNameContainingIgnoreCase(author)
            else -> logRepository.findAllByOrderByRequestTimeDesc()
        }
    }

    @GetMapping("/{id}/body")
    fun getLogBody(@PathVariable id: String): String? {
        return logService.getLogBody(id)
    }

    @PostMapping("/trace-flags")
    fun createTraceFlag(@RequestBody request: TraceFlagRequest): SalesforceCreateResponse? {
        return logService.createTraceFlag(request)
    }
}
