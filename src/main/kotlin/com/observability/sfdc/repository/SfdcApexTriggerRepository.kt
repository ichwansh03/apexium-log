package com.observability.sfdc.repository

import com.observability.sfdc.domain.SfdcApexTrigger
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface SfdcApexTriggerRepository : JpaRepository<SfdcApexTrigger, Long> {
    fun findBySfdcId(sfdcId: String): Optional<SfdcApexTrigger>
}
