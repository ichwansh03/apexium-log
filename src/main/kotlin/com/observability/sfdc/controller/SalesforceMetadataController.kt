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
        return metadataService.getAllDebugLevels(limit = size, offset = offset)
    }

    @GetMapping("/debug-levels/db")
    fun searchDebugLevels(
        @RequestParam(required = false) name: String?,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(defaultValue = "0") page: Int
    ): List<com.observability.sfdc.domain.DebugLevel> {
        val offset = page * size
        return metadataService.searchDebugLevels(name, size, offset)
    }

    @GetMapping("/classes")
    fun getApexClasses(
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(defaultValue = "0") page: Int
    ): List<ApexClassDto> {
        val offset = page * size
        return metadataService.getAllApexClasses(limit = size, offset = offset)
    }

    @GetMapping("/classes/db")
    fun searchClasses(
        @RequestParam(required = false) name: String?,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(defaultValue = "0") page: Int
    ): List<ApexClass> {
        val offset = page * size
        return metadataService.searchClasses(name, size, offset)
    }

    @GetMapping("/triggers")
    fun getApexTriggers(
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(defaultValue = "0") page: Int
    ): List<ApexTriggerDto> {
        val offset = page * size
        return metadataService.getAllApexTriggers(limit = size, offset = offset)
    }

    @GetMapping("/triggers/db")
    fun searchTriggers(
        @RequestParam(required = false) name: String?,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(defaultValue = "0") page: Int
    ): List<ApexTrigger> {
        val offset = page * size
        return metadataService.searchTriggers(name, size, offset)
    }
}
