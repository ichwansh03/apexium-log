package com.observability.sfdc.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class ApexLogDto(
    @JsonProperty("Id")
    val id: String,
    
    @JsonProperty("LogUser")
    val logUser: LogUserDto?,
    
    @JsonProperty("Operation")
    val operation: String?,
    
    @JsonProperty("StartTime")
    val startTime: String?,
    
    @JsonProperty("Status")
    val status: String?,
    
    @JsonProperty("LogLength")
    val logLength: Long?
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class LogUserDto(
    @JsonProperty("Name")
    val name: String?
)
