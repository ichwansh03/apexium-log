package com.observability.sfdc.repository

import com.observability.sfdc.domain.SfdcApexClass
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface SfdcApexClassRepository : JpaRepository<SfdcApexClass, Long> {
    fun findBySfdcId(sfdcId: String): Optional<SfdcApexClass>
}
