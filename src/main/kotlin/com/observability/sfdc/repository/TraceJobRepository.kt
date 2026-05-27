package com.observability.sfdc.repository

import com.observability.sfdc.domain.TraceJob
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TraceJobRepository : JpaRepository<TraceJob, Long> {
    fun findByStatus(status: String): List<TraceJob>
    fun findByTracedEntityIdAndStatus(tracedEntityId: String, status: String): Optional<TraceJob>
}
