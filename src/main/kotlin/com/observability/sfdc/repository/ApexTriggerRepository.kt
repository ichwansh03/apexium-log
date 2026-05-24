package com.observability.sfdc.repository

import com.observability.sfdc.domain.ApexTrigger
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ApexTriggerRepository : JpaRepository<ApexTrigger, Long> {
    fun findBySfdcId(sfdcId: String): Optional<ApexTrigger>
    fun findByNameContainingIgnoreCase(name: String, pageable: Pageable): List<ApexTrigger>
    fun findByNameContainingIgnoreCaseOrSobjectContainingIgnoreCase(name: String, sobject: String, pageable: Pageable): List<ApexTrigger>
    fun findAllProjectedBy(pageable: Pageable): List<ApexTrigger>
}
