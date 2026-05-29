package com.observability.sfdc.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank

@JsonIgnoreProperties(ignoreUnknown = true)
data class SalesforceUserDto(
    @JsonProperty("Id")
    @field:NotBlank(message = "Salesforce ID is required")
    val id: String,
    
    @JsonProperty("Name")
    @field:NotBlank(message = "Name is required")
    val name: String?,

    @JsonProperty("SFID")
    val sfdcId: String?,

    @JsonProperty("Username")
    val username: String?,
    
    @JsonProperty("Email")
    val email: String?,
    
    @JsonProperty("Profile")
    val profile: SalesforceProfileDto?,
    
    @JsonProperty("IsActive")
    val isActive: Boolean?,
    
    @JsonProperty("Entity__c")
    val entity: String?
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class SalesforceProfileDto(
    @JsonProperty("Name")
    val name: String?
)
