package com.observability.sfdc.domain

import jakarta.persistence.*

@Entity
@Table(name = "debug_levels")
data class DebugLevel(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "sfdc_id", unique = true)
    val sfdcId: String,

    val developerName: String?,
    val masterLabel: String?,
    val apexCode: String?,
    val apexProfiling: String?,
    val callout: String?,
    val database: String?,
    val system: String?,
    val validation: String?,
    val visualforce: String?,
    val workflow: String?
)
