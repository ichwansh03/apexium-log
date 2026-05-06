package com.observability.sfdc.domain

import jakarta.persistence.*

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "sfdc_id", unique = true)
    val sfdcId: String,

    val name: String?,
    val username: String?,
    val email: String?,
    val profileName: String?,
    val isActive: Boolean?,
    val entity: String?
)
