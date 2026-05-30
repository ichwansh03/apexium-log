package com.observability.sfdc.domain

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "trace_jobs")
data class TraceJob(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "traced_entity_id", nullable = false)
    val tracedEntityId: String,

    @Column(name = "traced_entity_name")
    val tracedEntityName: String?,

    @Column(name = "traced_entity_type", nullable = false)
    val tracedEntityType: String,

    @Column(name = "debug_level_name", nullable = false)
    val debugLevelName: String,

    @Column(name = "start_time", nullable = false)
    val startTime: Instant,

    @Column(name = "end_time", nullable = false)
    val endTime: Instant,

    @Column(nullable = false)
    var status: String, // ACTIVE, COMPLETED, CANCELLED

    @Column(name = "sfdc_trace_flag_id")
    var sfdcTraceFlagId: String? = null
)
