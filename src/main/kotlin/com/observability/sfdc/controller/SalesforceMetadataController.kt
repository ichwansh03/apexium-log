package com.observability.sfdc.controller

import com.observability.sfdc.dto.SalesforceApexClassDto
import com.observability.sfdc.dto.SalesforceApexTriggerDto
import com.observability.sfdc.service.SalesforceMetadataService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/sfdc/metadata")
class SalesforceMetadataController(
    private val metadataService: SalesforceMetadataService
) {

    @GetMapping("/classes")
    fun getApexClasses(
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(defaultValue = "0") page: Int
    ): List<SalesforceApexClassDto> {
        val offset = page * size
        return metadataService.getAllApexClasses(size, offset)
    }

    @GetMapping("/triggers")
    fun getApexTriggers(
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(defaultValue = "0") page: Int
    ): List<SalesforceApexTriggerDto> {
        val offset = page * size
        return metadataService.getAllApexTriggers(size, offset)
    }
}
