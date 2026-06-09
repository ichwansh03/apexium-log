package com.observability.sfdc.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank

@JsonIgnoreProperties(ignoreUnknown = true)
data class ApexTriggerDto(
    @JsonProperty("Id")
    @field:NotBlank(message = "Salesforce ID is required")
    val id: String,
    
    @JsonProperty("Name")
    @field:NotBlank(message = "Name is required")
    val name: String?,
    
    @JsonProperty("TableEnumOrId")
    val tableEnumOrId: String?,
    
    @JsonProperty("ApiVersion")
    val apiVersion: Double?,
    
    @JsonProperty("Status")
    val status: String?,

    @JsonProperty("UsageBeforeInsert")
    val usageBeforeInsert: Boolean?,

    @JsonProperty("UsageBeforeUpdate")
    val usageBeforeUpdate: Boolean?,

    @JsonProperty("UsageBeforeDelete")
    val usageBeforeDelete: Boolean?,

    @JsonProperty("UsageAfterInsert")
    val usageAfterInsert: Boolean?,

    @JsonProperty("UsageAfterUpdate")
    val usageAfterUpdate: Boolean?,

    @JsonProperty("UsageAfterDelete")
    val usageAfterDelete: Boolean?,

    @JsonProperty("UsageAfterUndelete")
    val usageAfterUndelete: Boolean?,
    
    @JsonProperty("LastModifiedDate")
    val lastModifiedDate: String?,

    @JsonProperty("LastModifiedBy")
    val lastModifiedBy: UserSummaryDto?,

    @JsonProperty("CreatedDate")
    val createdDate: String?,

    @JsonProperty("CreatedBy")
    val createdBy: UserSummaryDto?
)
