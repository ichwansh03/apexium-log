package com.observability.sfdc.service

import com.observability.sfdc.dto.SalesforceQueryResult
import com.observability.sfdc.dto.SalesforceApexClassDto
import com.observability.sfdc.dto.SalesforceApexTriggerDto
import com.observability.sfdc.repository.SfdcApexClassRepository
import com.observability.sfdc.repository.SfdcApexTriggerRepository
import com.observability.sfdc.domain.SfdcApexClass
import com.observability.sfdc.domain.SfdcApexTrigger
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
class SalesforceMetadataService(
    private val authService: SalesforceAuthService,
    private val classRepository: SfdcApexClassRepository,
    private val triggerRepository: SfdcApexTriggerRepository,
    @Value("\${salesforce.api-version}") private val apiVersion: String
) {
    private val restTemplate = RestTemplate()

    fun getAllApexClasses(limit: Int = 10, offset: Int = 0): List<SalesforceApexClassDto> {
        val tokenResponse = authService.getAccessToken() ?: return emptyList()
        
        val baseUrl = tokenResponse.instanceUrl
        val query = "SELECT Id, Name, ApiVersion, Status, LastModifiedDate FROM ApexClass ORDER BY Name ASC LIMIT $limit OFFSET $offset"
        
        val url = UriComponentsBuilder.fromUriString("$baseUrl/services/data/$apiVersion/tooling/query")
            .queryParam("q", query)
            .build()
            .toUriString()

        val headers = HttpHeaders()
        headers.setBearerAuth(tokenResponse.accessToken)
        
        val entity = HttpEntity<Unit>(headers)
        
        return try {
            val response: ResponseEntity<SalesforceQueryResult<SalesforceApexClassDto>> = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                object : ParameterizedTypeReference<SalesforceQueryResult<SalesforceApexClassDto>>() {}
            )
            val records = response.body?.records ?: emptyList()
            syncClassesToDatabase(records)
            records
        } catch (e: Exception) {
            println("Error querying ApexClasses: ${e.message}")
            emptyList()
        }
    }

    fun getAllApexTriggers(limit: Int = 10, offset: Int = 0): List<SalesforceApexTriggerDto> {
        val tokenResponse = authService.getAccessToken() ?: return emptyList()
        
        val baseUrl = tokenResponse.instanceUrl
        val query = "SELECT Id, Name, TableEnumOrId, ApiVersion, Status, LastModifiedDate FROM ApexTrigger ORDER BY Name ASC LIMIT $limit OFFSET $offset"
        
        val url = UriComponentsBuilder.fromUriString("$baseUrl/services/data/$apiVersion/tooling/query")
            .queryParam("q", query)
            .build()
            .toUriString()

        val headers = HttpHeaders()
        headers.setBearerAuth(tokenResponse.accessToken)
        
        val entity = HttpEntity<Unit>(headers)
        
        return try {
            val response: ResponseEntity<SalesforceQueryResult<SalesforceApexTriggerDto>> = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                object : ParameterizedTypeReference<SalesforceQueryResult<SalesforceApexTriggerDto>>() {}
            )
            val records = response.body?.records ?: emptyList()
            syncTriggersToDatabase(records)
            records
        } catch (e: Exception) {
            println("Error querying ApexTriggers: ${e.message}")
            emptyList()
        }
    }

    private fun syncClassesToDatabase(dtos: List<SalesforceApexClassDto>) {
        dtos.forEach { dto ->
            val existing = classRepository.findBySfdcId(dto.id)
            val apexClass = if (existing.isPresent) {
                existing.get().copy(
                    name = dto.name,
                    apiVersion = dto.apiVersion,
                    status = dto.status,
                    lastModifiedDate = dto.lastModifiedDate
                )
            } else {
                SfdcApexClass(
                    sfdcId = dto.id,
                    name = dto.name,
                    apiVersion = dto.apiVersion,
                    status = dto.status,
                    lastModifiedDate = dto.lastModifiedDate
                )
            }
            classRepository.save(apexClass)
        }
    }

    private fun syncTriggersToDatabase(dtos: List<SalesforceApexTriggerDto>) {
        dtos.forEach { dto ->
            val existing = triggerRepository.findBySfdcId(dto.id)
            val trigger = if (existing.isPresent) {
                existing.get().copy(
                    name = dto.name,
                    tableEnumOrId = dto.tableEnumOrId,
                    apiVersion = dto.apiVersion,
                    status = dto.status,
                    lastModifiedDate = dto.lastModifiedDate
                )
            } else {
                SfdcApexTrigger(
                    sfdcId = dto.id,
                    name = dto.name,
                    tableEnumOrId = dto.tableEnumOrId,
                    apiVersion = dto.apiVersion,
                    status = dto.status,
                    lastModifiedDate = dto.lastModifiedDate
                )
            }
            triggerRepository.save(trigger)
        }
    }
}
