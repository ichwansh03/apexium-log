package com.observability.sfdc.repository

import com.observability.sfdc.domain.Log
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LogRepository : JpaRepository<Log, Long> {
    fun findByApexClassNameContainingIgnoreCase(apexClassName: String): List<Log>
    fun findByAuthorNameContainingIgnoreCase(authorName: String): List<Log>
    fun findByApexClassNameContainingIgnoreCaseAndAuthorNameContainingIgnoreCase(apexClassName: String, authorName: String): List<Log>
}
