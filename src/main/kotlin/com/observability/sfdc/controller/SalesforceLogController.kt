package com.observability.sfdc.controller

import com.observability.sfdc.dto.ApexLogDto
import com.observability.sfdc.service.SalesforceLogService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/sfdc/logs")
class SalesforceLogController(
    private val logService: SalesforceLogService
) {

    @GetMapping
    fun getApexLogs(): List<ApexLogDto> {
        return logService.queryApexLogs()
    }

    @GetMapping("/{id}/body")
    fun getLogBody(@PathVariable id: String): String? {
        return logService.getLogBody(id)
    }
}
