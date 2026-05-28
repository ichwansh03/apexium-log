package com.observability.sfdc.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*

@Entity
@Table(name = "users")
@JsonIgnoreProperties(ignoreUnknown = true)
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
    
    @JsonProperty("active")
    val isActive: Boolean?,
    
    val entity: String?
)
