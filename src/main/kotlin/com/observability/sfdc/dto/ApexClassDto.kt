package com.observability.sfdc.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank

@JsonIgnoreProperties(ignoreUnknown = true)
data class ApexClassDto(
    @JsonProperty("Id")
    @field:NotBlank(message = "Salesforce ID is required")
    val id: String,
    
    @JsonProperty("Name")
    @field:NotBlank(message = "Name is required")
    val name: String?,
    
    @JsonProperty("ApiVersion")
    val apiVersion: Double?,
    
    @JsonProperty("Status")
    val status: String?,

    @JsonProperty("LengthWithoutComments")
    val lengthWithoutComments: Int?,
    
    @JsonProperty("LastModifiedDate")
    val lastModifiedDate: String?,

    @JsonProperty("LastModifiedBy")
    val lastModifiedBy: UserSummaryDto?,

    @JsonProperty("CreatedDate")
    val createdDate: String?,

    @JsonProperty("CreatedBy")
    val createdBy: UserSummaryDto?,

    val coverage: ApexCodeCoverageDto? = null,

    @JsonProperty("Body")
    val body: String? = null
)
