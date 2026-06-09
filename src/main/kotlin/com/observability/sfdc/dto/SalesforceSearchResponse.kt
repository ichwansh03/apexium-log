package com.observability.sfdc.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class SalesforceSearchResponse<T>(
    val searchRecords: List<T>
)
