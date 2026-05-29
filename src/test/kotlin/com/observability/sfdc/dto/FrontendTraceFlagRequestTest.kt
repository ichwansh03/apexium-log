package com.observability.sfdc.dto

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FrontendTraceFlagRequestTest {

    @Test
    fun `test getTotalMinutes with various inputs`() {
        val request1 = FrontendTraceFlagRequest(
            tracedEntityId = "testId",
            debugLevelName = "testLevel",
            durationDays = 1,
            durationHours = 2,
            durationMinutes = 30
        )
        // 1 day = 1440 min
        // 2 hours = 120 min
        // 30 min
        // Total = 1440 + 120 + 30 = 1590
        assertEquals(1590L, request1.getTotalMinutes())

        val request2 = FrontendTraceFlagRequest(
            tracedEntityId = "testId",
            debugLevelName = "testLevel",
            durationDays = null,
            durationHours = null,
            durationMinutes = 45
        )
        assertEquals(45L, request2.getTotalMinutes())

        val request3 = FrontendTraceFlagRequest(
            tracedEntityId = "testId",
            debugLevelName = "testLevel",
            durationDays = 0,
            durationHours = 5,
            durationMinutes = null
        )
        assertEquals(300L, request3.getTotalMinutes())
    }
}
