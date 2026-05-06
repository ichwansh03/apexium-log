package com.observability.sfdc.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class ApexLogDto(
    @JsonProperty("Id")
    val id: String,
    
    @JsonProperty("LogUser")
    val logUser: UserSummaryDto?,
    
    @JsonProperty("Operation")
    val operation: String?,
    
    @JsonProperty("StartTime")
    val startTime: String?,

    @JsonProperty("Status")
    val status: String?,

    @JsonProperty("Request")
    val request: String?,
    
    @JsonProperty("LogLength")
    val logLength: Long?,

    val apexClassName: String? = null
)
