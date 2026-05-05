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

    fun queryApexLogs(): List<ApexLogDto> {
        val tokenResponse = authService.getAccessToken() ?: return emptyList()
        
        val baseUrl = tokenResponse.instanceUrl
        val query = "SELECT Id, LogUserId, Operation, StartTime, Status, LogLength FROM ApexLog ORDER BY StartTime DESC LIMIT 10"
        
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
            response.body?.records ?: emptyList()
        } catch (e: Exception) {
            println("Error querying ApexLogs: ${e.message}")
            emptyList()
        }
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
