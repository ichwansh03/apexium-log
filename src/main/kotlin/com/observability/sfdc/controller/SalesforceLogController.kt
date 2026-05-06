package com.observability.sfdc.controller

import com.observability.sfdc.dto.ApexLogDto
import com.observability.sfdc.dto.SalesforceCreateResponse
import com.observability.sfdc.dto.TraceFlagRequest
import com.observability.sfdc.service.SalesforceLogService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/sfdc/logs")
class SalesforceLogController(
    private val logService: SalesforceLogService
) {

    @GetMapping
    fun getApexLogs(
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(defaultValue = "0") page: Int
    ): List<ApexLogDto> {
        val offset = page * size
        return logService.queryApexLogs(size, offset)
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
