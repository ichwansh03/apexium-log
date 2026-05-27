package com.observability.sfdc.service

import com.observability.sfdc.dto.*
import com.observability.sfdc.repository.DebugLevelRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.Cacheable
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Service
class SalesforceLogService(
    private val authService: SalesforceAuthService,
    private val debugLevelRepository: DebugLevelRepository,
    @Value($$"${salesforce.api-version}") private val apiVersion: String
) {
    private val restTemplate = RestTemplate()
    private val sfdcFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")

    fun queryApexLogs(limit: Int = 10, offset: Int = 0): List<ApexLogDto> {
        val tokenResponse = authService.getAccessToken() ?: return emptyList()
        
        val baseUrl = tokenResponse.instanceUrl
        val query = "SELECT Id, LogUser.Name, Operation, StartTime, Status, Request, LogLength, DurationMilliseconds FROM ApexLog ORDER BY StartTime DESC LIMIT $limit OFFSET $offset"
        
        val uri = UriComponentsBuilder.fromUriString("$baseUrl/services/data/$apiVersion/tooling/query")
            .queryParam("q", query)
            .build()
            .toUri()

        val headers = HttpHeaders()
        headers.setBearerAuth(tokenResponse.accessToken)
        
        val entity = HttpEntity<Unit>(headers)
        
        return try {
            val response: ResponseEntity<SalesforceQueryResult<ApexLogDto>> = restTemplate.exchange(
                uri,
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

        // Pattern to find the last CODE_UNIT_STARTED or CODE_UNIT_FINISHED which contains the entry point.
        // This is more reliable as it's typically at the end of the log and captures full context (Classes, Triggers, VF).
        val codeUnitRegex = Regex("\\|CODE_UNIT_(?:STARTED|FINISHED)\\|(?:.*\\|)?([^\\r\\n|]+)")
        val matches = codeUnitRegex.findAll(body).toList()

        if (matches.isNotEmpty()) {
            val fullPath = matches.last().groupValues[1].trim()

            // Handle Visualforce pages: VF: /apex/PageName -> extract PageName
            if (fullPath.startsWith("VF: /apex/")) {
                return fullPath.substringAfterLast("/")
            }

            // Handle Triggers: TriggerName on SObject -> keep full trigger context
            if (fullPath.contains(" on ", ignoreCase = true)) {
                return fullPath
            }

            // Handle Apex Classes: ClassName.methodName -> extract only ClassName
            // Taking the part before the last dot as the metadata name (handles namespaces correctly).
            return fullPath.substringBeforeLast(".")
        }

        // Fallback for standard Apex classes if no CODE_UNIT info is found
        val classRegex = Regex("\\|(?:METHOD_ENTRY|CLASS_ENTRY)\\|\\[[^]]*]\\|(?:[^|]*\\|)?([^.| \\n]+)")
        return classRegex.find(body)?.groupValues?.get(1)
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

    fun createTraceFlag(frontendRequest: FrontendTraceFlagRequest): SalesforceCreateResponse? {
        val tokenResponse = authService.getAccessToken() ?: return SalesforceCreateResponse(id = null, success = false, errors = listOf("Authentication failed"))
        
        // Resolve DebugLevel ID
        val debugLevels = debugLevelRepository.findAll()
        val debugLevel = debugLevels.find { it.developerName == frontendRequest.debugLevelName || it.masterLabel == frontendRequest.debugLevelName }
            ?: return SalesforceCreateResponse(id = null, success = false, errors = listOf("DebugLevel '${frontendRequest.debugLevelName}' not found. Please sync metadata first."))

        val expirationDate = ZonedDateTime.now(ZoneId.of("UTC"))
            .plusMinutes(frontendRequest.durationMinutes.toLong())
            .format(sfdcFormatter)

        val logType = when (frontendRequest.entityType) {
            "ApexClass", "ApexTrigger" -> "CLASS_TRACING"
            else -> "USER_DEBUG"
        }

        val sfdcRequest = TraceFlagRequest(
            tracedEntityId = frontendRequest.tracedEntityId,
            debugLevelId = debugLevel.sfdcId,
            logType = logType,
            expirationDate = expirationDate
        )

        val baseUrl = tokenResponse.instanceUrl
        val url = "$baseUrl/services/data/$apiVersion/tooling/sobjects/TraceFlag"

        val headers = HttpHeaders()
        headers.setBearerAuth(tokenResponse.accessToken)
        headers.contentType = MediaType.APPLICATION_JSON
        
        val entity = HttpEntity<TraceFlagRequest>(sfdcRequest, headers)
        
        return try {
            restTemplate.postForObject(url, entity, SalesforceCreateResponse::class.java)
        } catch (e: Exception) {
            println("Error creating TraceFlag: ${e.message}")
            SalesforceCreateResponse(id = null, success = false, errors = listOf(e.message ?: "Unknown error"))
        }
    }

    fun getActiveTraceFlags(): List<TraceFlagDto> {
        val tokenResponse = authService.getAccessToken() ?: return emptyList()
        
        val baseUrl = tokenResponse.instanceUrl
        val query = "SELECT Id, TracedEntityId, TracedEntity.Name, StartDate, ExpirationDate, DebugLevelId, DebugLevel.DeveloperName FROM TraceFlag WHERE ExpirationDate > ${ZonedDateTime.now(ZoneId.of("UTC")).format(sfdcFormatter)}"
        
        val uri = UriComponentsBuilder.fromUriString("$baseUrl/services/data/$apiVersion/tooling/query")
            .queryParam("q", query)
            .build()
            .toUri()

        val headers = HttpHeaders()
        headers.setBearerAuth(tokenResponse.accessToken)
        
        val entity = HttpEntity<Unit>(headers)
        
        return try {
            val response: ResponseEntity<SalesforceQueryResult<TraceFlagDto>> = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                entity,
                object : ParameterizedTypeReference<SalesforceQueryResult<TraceFlagDto>>() {}
            )
            response.body?.records ?: emptyList()
        } catch (e: Exception) {
            println("Error querying active TraceFlags: ${e.message}")
            emptyList()
        }
    }

    fun deleteTraceFlag(id: String): Boolean {
        val tokenResponse = authService.getAccessToken() ?: return false
        
        val baseUrl = tokenResponse.instanceUrl
        val url = "$baseUrl/services/data/$apiVersion/tooling/sobjects/TraceFlag/$id"

        val headers = HttpHeaders()
        headers.setBearerAuth(tokenResponse.accessToken)
        
        val entity = HttpEntity<Unit>(headers)
        
        return try {
            restTemplate.exchange(url, HttpMethod.DELETE, entity, Unit::class.java).statusCode.is2xxSuccessful
        } catch (e: Exception) {
            println("Error deleting TraceFlag $id: ${e.message}")
            false
        }
    }
}
