package com.observability.sfdc.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class ApexTriggerDto(
    @JsonProperty("Id")
    val id: String,
    
    @JsonProperty("Name")
    val name: String?,
    
    @JsonProperty("TableEnumOrId")
    val tableEnumOrId: String?,
    
    @JsonProperty("ApiVersion")
    val apiVersion: Double?,
    
    @JsonProperty("Status")
    val status: String?,
    
    @JsonProperty("LastModifiedDate")
    val lastModifiedDate: String?
)
