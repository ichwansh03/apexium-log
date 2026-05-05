package com.observability.sfdc.repository

import com.observability.sfdc.domain.Log
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LogRepository : JpaRepository<Log, Long> {
    fun findByApexClassNameContainingIgnoreCase(apexClassName: String): List<Log>
    fun findByAuthorContainingIgnoreCase(author: String): List<Log>
    fun findByApexClassNameContainingIgnoreCaseAndAuthorContainingIgnoreCase(apexClassName: String, author: String): List<Log>
}
