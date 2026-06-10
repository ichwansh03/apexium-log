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
    val lengthWithoutComments: Int?,
    val lastModifiedDate: String?,
    val lastModifiedByName: String?,
    val createdDate: String?,
    val createdByName: String?,
    
    val numLinesCovered: Int? = null,
    val numLinesUncovered: Int? = null
)
