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

    @JsonProperty("LengthWithoutComments")
    val lengthWithoutComments: Int?,
    
    @JsonProperty("LastModifiedDate")
    val lastModifiedDate: String?,

    @JsonProperty("LastModifiedBy")
    val lastModifiedBy: UserSummaryDto?,

    @JsonProperty("CreatedDate")
    val createdDate: String?,

    @JsonProperty("CreatedBy")
    val createdBy: UserSummaryDto?
)
