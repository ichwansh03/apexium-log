package com.observability.sfdc.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class MetadataDetailDto(
    val id: String,
    val name: String,
    val type: String, // "ApexClass" or "ApexTrigger"
    val apiVersion: Double?,
    val status: String?,
    val lastModifiedDate: String?,
    val lastModifiedByName: String?,
    val targetObject: String? = null, // For Triggers: TableEnumOrId
    val triggerEvents: List<String> = emptyList(),
    val testClasses: List<ApexClassDto> = emptyList(),
    val coverage: ApexCodeCoverageDto? = null,
    val body: String? = null
)
