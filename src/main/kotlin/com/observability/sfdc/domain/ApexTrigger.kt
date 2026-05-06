package com.observability.sfdc.domain

import jakarta.persistence.*

@Entity
@Table(name = "apex_triggers")
data class ApexTrigger(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "sfdc_id", unique = true)
    val sfdcId: String,

    val name: String?,
    val tableEnumOrId: String?,
    val apiVersion: Double?,
    val status: String?,
    val lastModifiedDate: String?
)
