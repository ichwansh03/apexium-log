package com.observability.sfdc.service

import com.observability.sfdc.dto.ApexLogDto
import com.observability.sfdc.dto.SalesforceQueryResult
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

@Service
class SalesforceLogService(
    private val authService: SalesforceAuthService,
    @Value("\${salesforce.api-version}") private val apiVersion: String
) {
    private val restTemplate = RestTemplate()

    fun queryApexLogs(limit: Int = 10, offset: Int = 0): List<ApexLogDto> {
        val tokenResponse = authService.getAccessToken() ?: return emptyList()
        
        val baseUrl = tokenResponse.instanceUrl
        val query = "SELECT Id, LogUser.Name, Operation, StartTime, Status, LogLength FROM ApexLog ORDER BY StartTime DESC LIMIT $limit OFFSET $offset"
        
        val url = UriComponentsBuilder.fromUriString("$baseUrl/services/data/$apiVersion/tooling/query")
            .queryParam("q", query)
            .build()
            .toUriString()

        val headers = HttpHeaders()
        headers.setBearerAuth(tokenResponse.accessToken)
        
        val entity = HttpEntity<Unit>(headers)
        
        return try {
            val response: ResponseEntity<SalesforceQueryResult<ApexLogDto>> = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                object : ParameterizedTypeReference<SalesforceQueryResult<ApexLogDto>>() {}
            )
            val records = response.body?.records ?: emptyList()
            
            // Enrich records with Apex Class Name by fetching bodies
            records.map { dto ->
                val body = getLogBody(dto.id)
                dto.copy(apexClassName = extractClassName(body))
            }
        } catch (e: Exception) {
            println("Error querying ApexLogs: ${e.message}")
            emptyList()
        }
    }

    private fun extractClassName(body: String?): String? {
        if (body == null) return null

        // Pattern 1: CODE_UNIT_STARTED for triggers and other units
        // Example: |CODE_UNIT_STARTED|[EXTERNAL]|01q...|LogEventTrigger on AppLog__ChangeEvent...
        val triggerRegex = Regex("\\|CODE_UNIT_STARTED\\|\\[[^]]*]\\|[^|]*\\|([^\\s]+)(?:\\son|\\strigger)")
        val triggerMatch = triggerRegex.find(body)
        if (triggerMatch != null) return triggerMatch.groupValues[1]

        // Pattern 2: METHOD_ENTRY or CLASS_ENTRY for standard Apex classes
        // Example: |METHOD_ENTRY|[5]|01p...|MyController.doSomething()
        val classRegex = Regex("\\|(?:METHOD_ENTRY|CLASS_ENTRY)\\|\\[[^]]*]\\|(?:[^|]*\\|)?([^.| \\n]+)")
        val classMatch = classRegex.find(body)

        return classMatch?.groupValues?.get(1)
    }

    fun getLogBody(logId: String): String? {
        val tokenResponse = authService.getAccessToken() ?: return null
        
        val baseUrl = tokenResponse.instanceUrl
        val url = "$baseUrl/services/data/$apiVersion/tooling/sobjects/ApexLog/$logId/Body"

        val headers = HttpHeaders()
        headers.setBearerAuth(tokenResponse.accessToken)
        
        val entity = HttpEntity<Unit>(headers)
        
        return try {
            val response: ResponseEntity<String> = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String::class.java
            )
            response.body
        } catch (e: Exception) {
            println("Error fetching log body for $logId: ${e.message}")
            null
        }
    }
}
