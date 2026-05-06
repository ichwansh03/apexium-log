package com.observability.sfdc.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class ApexClassDto(
    @JsonProperty("Id")
    val id: String,
    
    @JsonProperty("Name")
    val name: String?,
    
    @JsonProperty("ApiVersion")
    val apiVersion: Double?,
    
    @JsonProperty("Status")
    val status: String?,
    
    @JsonProperty("LastModifiedDate")
    val lastModifiedDate: String?
)
