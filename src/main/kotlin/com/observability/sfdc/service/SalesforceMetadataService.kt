package com.observability.sfdc.service

import com.observability.sfdc.dto.SalesforceQueryResult
import com.observability.sfdc.dto.ApexClassDto
import com.observability.sfdc.dto.ApexTriggerDto
import com.observability.sfdc.repository.ApexClassRepository
import com.observability.sfdc.repository.ApexTriggerRepository
import com.observability.sfdc.domain.ApexClass
import com.observability.sfdc.domain.ApexTrigger
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
    private val classRepository: ApexClassRepository,
    private val triggerRepository: ApexTriggerRepository,
    @Value("\${salesforce.api-version}") private val apiVersion: String
) {
    private val restTemplate = RestTemplate()

    fun getAllApexClasses(limit: Int = 10, offset: Int = 0): List<ApexClassDto> {
        val tokenResponse = authService.getAccessToken() ?: return emptyList()
        
        val baseUrl = tokenResponse.instanceUrl
        val query = "SELECT Id, Name, ApiVersion, Status, LengthWithoutComments, LastModifiedDate, LastModifiedBy.Name, CreatedDate, CreatedBy.Name FROM ApexClass WHERE Status = 'Active' ORDER BY Name ASC LIMIT $limit OFFSET $offset"
        
        val url = UriComponentsBuilder.fromUriString("$baseUrl/services/data/$apiVersion/tooling/query")
            .queryParam("q", query)
            .build()
            .toUriString()

        val headers = HttpHeaders()
        headers.setBearerAuth(tokenResponse.accessToken)
        
        val entity = HttpEntity<Unit>(headers)
        
        return try {
            val response: ResponseEntity<SalesforceQueryResult<ApexClassDto>> = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                object : ParameterizedTypeReference<SalesforceQueryResult<ApexClassDto>>() {}
            )
            val records = response.body?.records ?: emptyList()
            syncClassesToDatabase(records)
            records
        } catch (e: Exception) {
            println("Error querying ApexClasses: ${e.message}")
            emptyList()
        }
    }

    fun getAllApexTriggers(limit: Int = 10, offset: Int = 0): List<ApexTriggerDto> {
        val tokenResponse = authService.getAccessToken() ?: return emptyList()
        
        val baseUrl = tokenResponse.instanceUrl
        val query = "SELECT Id, Name, TableEnumOrId, ApiVersion, Status, UsageBeforeInsert, UsageBeforeUpdate, UsageBeforeDelete, UsageAfterInsert, UsageAfterUpdate, UsageAfterDelete, LastModifiedDate, LastModifiedBy.Name, CreatedDate, CreatedBy.Name FROM ApexTrigger WHERE Status = 'Active' ORDER BY Name ASC LIMIT $limit OFFSET $offset"
        
        val url = UriComponentsBuilder.fromUriString("$baseUrl/services/data/$apiVersion/tooling/query")
            .queryParam("q", query)
            .build()
            .toUriString()

        val headers = HttpHeaders()
        headers.setBearerAuth(tokenResponse.accessToken)
        
        val entity = HttpEntity<Unit>(headers)
        
        return try {
            val response: ResponseEntity<SalesforceQueryResult<ApexTriggerDto>> = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                object : ParameterizedTypeReference<SalesforceQueryResult<ApexTriggerDto>>() {}
            )
            val records = response.body?.records ?: emptyList()
            syncTriggersToDatabase(records)
            records
        } catch (e: Exception) {
            println("Error querying ApexTriggers: ${e.message}")
            emptyList()
        }
    }

    private fun syncClassesToDatabase(dtos: List<ApexClassDto>) {
        dtos.forEach { dto ->
            val existing = classRepository.findBySfdcId(dto.id)
            val apexClass = if (existing.isPresent) {
                existing.get().copy(
                    name = dto.name,
                    apiVersion = dto.apiVersion,
                    status = dto.status,
                    lengthWithoutComments = dto.lengthWithoutComments,
                    lastModifiedDate = dto.lastModifiedDate,
                    lastModifiedByName = dto.lastModifiedBy?.name,
                    createdDate = dto.createdDate,
                    createdByName = dto.createdBy?.name
                )
            } else {
                ApexClass(
                    sfdcId = dto.id,
                    name = dto.name,
                    apiVersion = dto.apiVersion,
                    status = dto.status,
                    lengthWithoutComments = dto.lengthWithoutComments,
                    lastModifiedDate = dto.lastModifiedDate,
                    lastModifiedByName = dto.lastModifiedBy?.name,
                    createdDate = dto.createdDate,
                    createdByName = dto.createdBy?.name
                )
            }
            classRepository.save(apexClass)
        }
    }

    private fun syncTriggersToDatabase(dtos: List<ApexTriggerDto>) {
        dtos.forEach { dto ->
            val existing = triggerRepository.findBySfdcId(dto.id)
            val trigger = if (existing.isPresent) {
                existing.get().copy(
                    name = dto.name,
                    tableEnumOrId = dto.tableEnumOrId,
                    apiVersion = dto.apiVersion,
                    status = dto.status,
                    usageBeforeInsert = dto.usageBeforeInsert,
                    usageBeforeUpdate = dto.usageBeforeUpdate,
                    usageBeforeDelete = dto.usageBeforeDelete,
                    usageAfterInsert = dto.usageAfterInsert,
                    usageAfterUpdate = dto.usageAfterUpdate,
                    usageAfterDelete = dto.usageAfterDelete,
                    lastModifiedDate = dto.lastModifiedDate,
                    lastModifiedByName = dto.lastModifiedBy?.name,
                    createdDate = dto.createdDate,
                    createdByName = dto.createdBy?.name
                )
            } else {
                ApexTrigger(
                    sfdcId = dto.id,
                    name = dto.name,
                    tableEnumOrId = dto.tableEnumOrId,
                    apiVersion = dto.apiVersion,
                    status = dto.status,
                    usageBeforeInsert = dto.usageBeforeInsert,
                    usageBeforeUpdate = dto.usageBeforeUpdate,
                    usageBeforeDelete = dto.usageBeforeDelete,
                    usageAfterInsert = dto.usageAfterInsert,
                    usageAfterUpdate = dto.usageAfterUpdate,
                    usageAfterDelete = dto.usageAfterDelete,
                    lastModifiedDate = dto.lastModifiedDate,
                    lastModifiedByName = dto.lastModifiedBy?.name,
                    createdDate = dto.createdDate,
                    createdByName = dto.createdBy?.name
                )
            }
            triggerRepository.save(trigger)
        }
    }
}
