package com.observability.sfdc.domain

import jakarta.persistence.*

@Entity
@Table(name = "sfdc_apex_classes")
data class SfdcApexClass(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "sfdc_id", unique = true)
    val sfdcId: String,

    val name: String?,
    val apiVersion: Double?,
    val status: String?,
    val lastModifiedDate: String?
)
