package com.observability.sfdc.repository

import com.observability.sfdc.domain.DebugLevel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface DebugLevelRepository : JpaRepository<DebugLevel, Long> {
    fun findBySfdcId(sfdcId: String): Optional<DebugLevel>
}
