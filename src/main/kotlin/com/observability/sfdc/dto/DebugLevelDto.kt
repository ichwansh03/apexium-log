package com.observability.sfdc.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank

@JsonIgnoreProperties(ignoreUnknown = true)
data class DebugLevelDto(
    @JsonProperty("Id")
    @field:NotBlank(message = "Salesforce ID is required")
    val id: String,
    
    @JsonProperty("DeveloperName")
    @field:NotBlank(message = "DeveloperName is required")
    val developerName: String?,
    
    @JsonProperty("MasterLabel")
    val masterLabel: String?,
    
    @JsonProperty("ApexCode")
    val apexCode: String?,
    
    @JsonProperty("ApexProfiling")
    val apexProfiling: String?,
    
    @JsonProperty("Callout")
    val callout: String?,
    
    @JsonProperty("Database")
    val database: String?,
    
    @JsonProperty("System")
    val system: String?,
    
    @JsonProperty("Validation")
    val validation: String?,
    
    @JsonProperty("Visualforce")
    val visualforce: String?,
    
    @JsonProperty("Workflow")
    val workflow: String?
)
