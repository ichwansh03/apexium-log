package com.observability.sfdc.controller

import com.observability.sfdc.domain.ApexClass
import com.observability.sfdc.domain.ApexTrigger
import com.observability.sfdc.dto.ApexClassDto
import com.observability.sfdc.dto.ApexTriggerDto
import com.observability.sfdc.dto.DebugLevelDto
import com.observability.sfdc.service.SalesforceMetadataService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

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

    @GetMapping("/debug-levels/db")
    fun searchDebugLevels(
        @RequestParam(required = false) name: String?
    ): List<com.observability.sfdc.domain.DebugLevel> {
        return metadataService.searchDebugLevels(name)
    }

    @GetMapping("/classes")
    fun getApexClasses(
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(defaultValue = "0") page: Int
    ): List<ApexClassDto> {
        val offset = page * size
        return metadataService.getAllApexClasses(size, offset)
    }

    @GetMapping("/classes/db")
    fun searchClasses(
        @RequestParam(required = false) name: String?
    ): List<ApexClass> {
        return metadataService.searchClasses(name)
    }

    @GetMapping("/triggers")
    fun getApexTriggers(
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(defaultValue = "0") page: Int
    ): List<ApexTriggerDto> {
        val offset = page * size
        return metadataService.getAllApexTriggers(size, offset)
    }

    @GetMapping("/triggers/db")
    fun searchTriggers(
        @RequestParam(required = false) name: String?
    ): List<ApexTrigger> {
        return metadataService.searchTriggers(name)
    }
}
