package com.observability.sfdc.dto

import java.time.LocalDateTime

data class LogDto(
    val id: Long? = null,
    val apexClassName: String? = null,
    val author: String? = null,
    val requestTime: LocalDateTime? = null,
    val operation: String? = null,
    val logSize: Long? = null,
    val duration: Long? = null,
    val status: String? = null
)
