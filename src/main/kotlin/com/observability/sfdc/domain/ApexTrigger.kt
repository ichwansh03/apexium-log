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
    val usageBeforeInsert: Boolean?,
    val usageBeforeUpdate: Boolean?,
    val usageBeforeDelete: Boolean?,
    val usageAfterInsert: Boolean?,
    val usageAfterUpdate: Boolean?,
    val usageAfterDelete: Boolean?,
    val lastModifiedDate: String?,
    val lastModifiedByName: String?,
    val createdDate: String?,
    val createdByName: String?
)
