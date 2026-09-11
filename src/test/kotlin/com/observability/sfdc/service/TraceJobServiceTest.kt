package com.observability.sfdc.service

import com.observability.sfdc.domain.TraceJob
import com.observability.sfdc.dto.FrontendTraceFlagRequest
import com.observability.sfdc.repository.TraceJobRepository
import com.observability.sfdc.service.impl.OrgContextService
import com.observability.sfdc.service.impl.TraceJobService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.*

class TraceJobServiceTest {

    private val traceJobRepository = mock(TraceJobRepository::class.java)
    private val logService = mock(TraceFlagService::class.java)
    private val orgContextService = mock(OrgContextService::class.java)
    private val traceJobService = TraceJobService(traceJobRepository, logService, orgContextService)

    @Test
    fun `createJob should calculate correct endTime with days hours and minutes`() {
        // Arrange
        val testOrgId = "00D000000000001"
        `when`(orgContextService.getActiveOrgId()).thenReturn(testOrgId)

        val request = FrontendTraceFlagRequest(
            tracedEntityId = "testId",
            debugLevelName = "testLevel",
            durationDays = 1,
            durationHours = 2,
            durationMinutes = 30
        )
        
        `when`(traceJobRepository.save(any(TraceJob::class.java))).thenAnswer { it.arguments[0] as TraceJob }

        // Act
        val job = traceJobService.createJob(request)

        // Assert
        assertEquals("testId", job.tracedEntityId)
        assertEquals("testLevel", job.debugLevelName)
        assertEquals("ACTIVE", job.status)
        assertEquals(testOrgId, job.orgId)
        
        verify(traceJobRepository, times(1)).save(any(TraceJob::class.java))
    }
}
