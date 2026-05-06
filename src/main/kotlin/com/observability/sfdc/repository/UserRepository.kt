package com.observability.sfdc.repository

import com.observability.sfdc.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findBySfdcId(sfdcId: String): Optional<User>
}
