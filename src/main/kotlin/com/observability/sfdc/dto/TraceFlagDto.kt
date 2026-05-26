package com.observability.sfdc.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

@JsonIgnoreProperties(ignoreUnknown = true)
data class TraceFlagRequest(
    @JsonProperty("TracedEntityId")
    @field:NotBlank(message = "TracedEntityId is required")
    val tracedEntityId: String,
    
    @JsonProperty("DebugLevelId")
    @field:NotBlank(message = "DebugLevelId is required")
    val debugLevelId: String,
    
    @JsonProperty("LogType")
    @field:NotBlank(message = "LogType is required")
    val logType: String,
    
    @JsonProperty("StartDate")
    val startDate: String? = null,
    
    @JsonProperty("ExpirationDate")
    @field:NotBlank(message = "ExpirationDate is required")
    val expirationDate: String
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class FrontendTraceFlagRequest(
    @field:NotBlank(message = "TracedEntityId is required")
    val tracedEntityId: String,
    
    @field:NotBlank(message = "DebugLevelName is required")
    val debugLevelName: String,
    
    @field:NotNull(message = "DurationMinutes is required")
    @field:Min(value = 1, message = "Duration must be at least 1 minute")
    val durationMinutes: Int,
    
    val entityType: String? = "User"
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class SalesforceCreateResponse(
    val id: String?,
    val success: Boolean,
    val errors: List<String> = emptyList()
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class TraceFlagDto(
    @JsonProperty("Id")
    val id: String,
    @JsonProperty("TracedEntityId")
    val tracedEntityId: String,
    @JsonProperty("TracedEntity")
    val tracedEntity: TracedEntityDto?,
    @JsonProperty("StartDate")
    val startDate: String?,
    @JsonProperty("ExpirationDate")
    val expirationDate: String?,
    @JsonProperty("DebugLevelId")
    val debugLevelId: String?,
    @JsonProperty("DebugLevel")
    val debugLevel: DebugLevelSummaryDto?
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class TracedEntityDto(
    @JsonProperty("Name")
    val name: String?,
    @JsonProperty("attributes")
    val attributes: EntityAttributesDto?
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class EntityAttributesDto(
    @JsonProperty("type")
    val type: String?
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class DebugLevelSummaryDto(
    @JsonProperty("DeveloperName")
    val developerName: String?
)
