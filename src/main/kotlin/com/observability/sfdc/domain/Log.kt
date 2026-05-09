package com.observability.sfdc.domain

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "logs")
data class Log(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "sfdc_id", unique = true)
    val sfdcId: String,

    @Column(name = "apex_class_name")
    val apexClassName: String? = null,

    @Column(name = "author_name")
    val authorName: String? = null,

    @Column(name = "request_time")
    val requestTime: LocalDateTime? = null,

    @Column(name = "operation")
    val operation: String? = null,

    @Column(name = "log_size")
    val logSize: Long? = null,

    @Column(name = "duration")
    val duration: Long? = null,

    @Column(name = "status")
    val status: String? = null,

    @Column(name = "request")
    val request: String? = null
)
