package com.observability.sfdc.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class SalesforceUserDto(
    @JsonProperty("Id")
    val id: String,
    
    @JsonProperty("Name")
    val name: String?,
    
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
