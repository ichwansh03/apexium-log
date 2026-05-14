package com.observability.sfdc.repository

import com.observability.sfdc.domain.ApexTrigger
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface ApexTriggerRepository : JpaRepository<ApexTrigger, Long> {
    fun findBySfdcId(sfdcId: String): Optional<ApexTrigger>
    fun findByNameContainingIgnoreCase(name: String): List<ApexTrigger>
}
