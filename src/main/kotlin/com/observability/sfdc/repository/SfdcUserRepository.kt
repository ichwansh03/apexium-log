package com.observability.sfdc.repository

import com.observability.sfdc.domain.SfdcUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface SfdcUserRepository : JpaRepository<SfdcUser, Long> {
    fun findBySfdcId(sfdcId: String): Optional<SfdcUser>
}
