package com.observability.sfdc.service

import com.observability.sfdc.domain.Log
import com.observability.sfdc.dto.ApexLogDto
import com.observability.sfdc.dto.UserSummaryDto
import com.observability.sfdc.repository.LogRepository
import com.observability.sfdc.service.impl.OrgContextService
import com.observability.sfdc.service.impl.SalesforceLogPollingService
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import java.util.*

class SalesforceLogPollingServiceTest {

    private val logService = mock(ApexLogService::class.java)
    private val logRepository = mock(LogRepository::class.java)
    private val orgContextService = mock(OrgContextService::class.java)
    private val pollingService = SalesforceLogPollingService(logService, logRepository, orgContextService)

    @Test
    fun `pollLogs should save new logs and skip existing ones`() {
        // Arrange
        val logId1 = "07L...1"
        val logId2 = "07L...2"
        val testOrgId = "00D000000000001"

        `when`(orgContextService.getActiveOrgId()).thenReturn(testOrgId)

        val logs = listOf(
            ApexLogDto(id = logId1, logUser = UserSummaryDto("User 1"), operation = "Op 1", startTime = "2026-05-09T10:00:00.000+0000", status = "Success", request = "Req 1", logLength = 100, durationMilliseconds = 50),
            ApexLogDto(id = logId2, logUser = UserSummaryDto("User 2"), operation = "Op 2", startTime = "2026-05-09T10:01:00.000+0000", status = "Success", request = "Req 2", logLength = 200, durationMilliseconds = 150)
        )

        `when`(logService.queryApexLogs(limit = 20, fetchBody = false)).thenReturn(logs)
        `when`(logRepository.existsByOrgIdAndSfdcId(testOrgId, logId1)).thenReturn(true)
        `when`(logRepository.existsByOrgIdAndSfdcId(testOrgId, logId2)).thenReturn(false)

        // Act
        pollingService.pollLogs()

        // Assert
        verify(logRepository, times(1)).save(any(Log::class.java))
        verify(logRepository, times(1)).existsByOrgIdAndSfdcId(testOrgId, logId1)
        verify(logRepository, times(1)).existsByOrgIdAndSfdcId(testOrgId, logId2)
    }
}
