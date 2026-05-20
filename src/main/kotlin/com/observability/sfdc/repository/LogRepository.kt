package com.observability.sfdc.repository

import com.observability.sfdc.domain.Log
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface LogRepository : JpaRepository<Log, Long> {
    fun findBySfdcId(sfdcId: String): Optional<Log>
    fun findAllByOrderByRequestTimeDesc(): List<Log>
    fun findByApexClassNameContainingIgnoreCase(apexClassName: String): List<Log>
    fun findByAuthorNameContainingIgnoreCase(authorName: String): List<Log>
    fun findByApexClassNameContainingIgnoreCaseAndAuthorNameContainingIgnoreCase(apexClassName: String, authorName: String): List<Log>
}
