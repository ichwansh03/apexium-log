package com.observability.sfdc.repository

import com.observability.sfdc.domain.User
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findBySfdcId(sfdcId: String): Optional<User>
    fun findByNameContainingIgnoreCase(name: String, pageable: Pageable): List<User>
    fun findByUsernameContainingIgnoreCase(username: String, pageable: Pageable): List<User>
    fun findAllProjectedBy(pageable: Pageable): List<User>
}
