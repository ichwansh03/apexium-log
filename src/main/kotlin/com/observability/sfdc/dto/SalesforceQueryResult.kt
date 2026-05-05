package com.observability.sfdc.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class SalesforceQueryResult<T>(
    val totalSize: Int,
    val done: Boolean,
    val records: List<T>
)
