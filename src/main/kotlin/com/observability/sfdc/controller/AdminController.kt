package com.observability.sfdc.controller

import com.observability.sfdc.service.impl.OrgContextService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin")
@Tag(name = "Admin", description = "Administrative endpoints for cache and org management")
class AdminController(
    private val orgContextService: OrgContextService
) {

    @PostMapping("/cache/flush")
    @Operation(summary = "Flush Salesforce Caches", description = "Manually flush all Salesforce-related Redis cache keys (sf_tokens, sf_metadata, sf_users).")
    fun flushCache(): ResponseEntity<Map<String, Any>> {
        orgContextService.flushAllSalesforceCaches()
        return ResponseEntity.ok(mapOf(
            "message" to "All Salesforce caches flushed successfully",
            "activeOrgId" to orgContextService.getActiveOrgId()
        ))
    }
}
