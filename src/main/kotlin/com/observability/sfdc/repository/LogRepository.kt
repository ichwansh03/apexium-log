package com.observability.sfdc.repository

import com.observability.sfdc.domain.Log
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.*

@Repository
interface LogRepository : JpaRepository<Log, Long> {
    fun findBySfdcId(sfdcId: String): Optional<Log>
    fun deleteBySfdcId(sfdcId: String)
    fun findAllByOrderByRequestTimeDesc(pageable: Pageable): List<Log>
    fun findByApexClassNameContainingIgnoreCase(apexClassName: String, pageable: Pageable): List<Log>
    fun findByAuthorNameContainingIgnoreCase(authorName: String, pageable: Pageable): List<Log>
    fun findByApexClassNameContainingIgnoreCaseAndAuthorNameContainingIgnoreCase(apexClassName: String, authorName: String, pageable: Pageable): List<Log>
    fun deleteByRequestTimeBefore(cutoff: Instant): Int
}
