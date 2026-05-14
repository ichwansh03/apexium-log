package com.observability.sfdc.repository

import com.observability.sfdc.domain.ApexClass
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface ApexClassRepository : JpaRepository<ApexClass, Long> {
    fun findBySfdcId(sfdcId: String): Optional<ApexClass>
    fun findByNameContainingIgnoreCase(name: String): List<ApexClass>
}
