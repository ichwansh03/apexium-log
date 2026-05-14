package com.observability.sfdc.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class TraceFlagRequest(
    @JsonProperty("TracedEntityId")
    val tracedEntityId: String,
    
    @JsonProperty("DebugLevelId")
    val debugLevelId: String,
    
    @JsonProperty("LogType")
    val logType: String = "USER_DEBUG",
    
    @JsonProperty("StartDate")
    val startDate: String? = null,
    
    @JsonProperty("ExpirationDate")
    val expirationDate: String
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class FrontendTraceFlagRequest(
    val tracedEntityId: String,
    val debugLevelName: String,
    val durationMinutes: Int
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
