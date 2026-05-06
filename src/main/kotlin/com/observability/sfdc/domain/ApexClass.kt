package com.observability.sfdc.domain

import jakarta.persistence.*

@Entity
@Table(name = "apex_classes")
data class ApexClass(
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
