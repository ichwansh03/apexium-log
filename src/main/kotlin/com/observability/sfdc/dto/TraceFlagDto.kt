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
