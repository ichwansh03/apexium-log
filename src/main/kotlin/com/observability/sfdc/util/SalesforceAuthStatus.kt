package com.observability.sfdc.util

import org.springframework.stereotype.Component
import kotlin.time.Clock
import kotlin.time.Instant

@Component
class SalesforceAuthStatus {
    @Volatile var lastSuccess: Boolean = true
    @Volatile var lastError: String? = null
    @Volatile var lastCheckedAt: Instant? = Clock.System.now()

    fun markSuccess() {
        lastSuccess = true
        lastError = null
        lastCheckedAt = Clock.System.now()
    }

    fun markError(message: String) {
        lastSuccess = false
        lastError = message
        lastCheckedAt = Clock.System.now()
    }
}