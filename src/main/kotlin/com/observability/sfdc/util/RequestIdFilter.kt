package com.observability.sfdc.util

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.MDC
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.util.UUID

/**
 * Propagates a request correlation id through the MDC so every log line for a
 * request carries the same `requestId`, and echoes it back in the response
 * header. Accepts a client-supplied id (sanitized) or generates a new one.
 */
@Component
class RequestIdFilter : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val requestId = sanitize(request.getHeader(REQUEST_ID_HEADER))
            ?: UUID.randomUUID().toString()

        MDC.put(MDC_KEY, requestId)
        response.setHeader(REQUEST_ID_HEADER, requestId)
        try {
            filterChain.doFilter(request, response)
        } finally {
            MDC.remove(MDC_KEY)
        }
    }

    private fun sanitize(value: String?): String? {
        if (value.isNullOrBlank()) return null
        val trimmed = value.trim()
        if (trimmed.length > MAX_LENGTH) return null
        return trimmed.takeIf { SAFE_PATTERN.matches(it) }
    }

    companion object {
        const val REQUEST_ID_HEADER = "X-Request-ID"
        const val MDC_KEY = "requestId"

        private const val MAX_LENGTH = 64
        private val SAFE_PATTERN = Regex("^[A-Za-z0-9._-]+$")
    }
}
