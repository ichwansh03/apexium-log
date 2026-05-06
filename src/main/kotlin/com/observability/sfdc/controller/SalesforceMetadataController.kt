package com.observability.sfdc.controller

import com.observability.sfdc.dto.ApexClassDto
import com.observability.sfdc.dto.ApexTriggerDto
import com.observability.sfdc.dto.DebugLevelDto
import com.observability.sfdc.service.SalesforceMetadataService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/sfdc/metadata")
class SalesforceMetadataController(
    private val metadataService: SalesforceMetadataService
) {

    @GetMapping("/debug-levels")
    fun getDebugLevels(
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(defaultValue = "0") page: Int
    ): List<DebugLevelDto> {
        val offset = page * size
        return metadataService.getAllDebugLevels(size, offset)
    }

    @GetMapping("/classes")
    fun getApexClasses(
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(defaultValue = "0") page: Int
    ): List<ApexClassDto> {
        val offset = page * size
        return metadataService.getAllApexClasses(size, offset)
    }

    @GetMapping("/triggers")
    fun getApexTriggers(
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(defaultValue = "0") page: Int
    ): List<ApexTriggerDto> {
        val offset = page * size
        return metadataService.getAllApexTriggers(size, offset)
    }
}
