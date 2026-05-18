package com.observability.sfdc.service

import com.observability.sfdc.dto.SalesforceQueryResult
import com.observability.sfdc.dto.ApexClassDto
import com.observability.sfdc.dto.ApexTriggerDto
import com.observability.sfdc.dto.DebugLevelDto
import com.observability.sfdc.repository.ApexClassRepository
import com.observability.sfdc.repository.ApexTriggerRepository
import com.observability.sfdc.repository.DebugLevelRepository
import com.observability.sfdc.domain.ApexClass
import com.observability.sfdc.domain.ApexTrigger
import com.observability.sfdc.domain.DebugLevel
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.Cacheable
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
    private val debugLevelRepository: DebugLevelRepository,
    @Value($$"${salesforce.api-version}") private val apiVersion: String
) {
    private val restTemplate = RestTemplate()

    @Cacheable(value = ["sf_metadata"], key = "'debug_levels_' + #limit + '_' + #offset", unless = "#result == null")
    fun getAllDebugLevels(limit: Int = 10, offset: Int = 0): List<DebugLevelDto> {
        val tokenResponse = authService.getAccessToken() ?: return emptyList()
        
        val baseUrl = tokenResponse.instanceUrl
        val query = "SELECT Id, DeveloperName, MasterLabel, ApexCode, ApexProfiling, Callout, Database, System, Validation, Visualforce, Workflow FROM DebugLevel LIMIT $limit OFFSET $offset"
        
        val uri = UriComponentsBuilder.fromUriString("$baseUrl/services/data/$apiVersion/tooling/query")
            .queryParam("q", query)
            .build()
            .toUri()

        val headers = HttpHeaders()
        headers.setBearerAuth(tokenResponse.accessToken)
        
        val entity = HttpEntity<Unit>(headers)
        
        return try {
            val response: ResponseEntity<SalesforceQueryResult<DebugLevelDto>> = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                entity,
                object : ParameterizedTypeReference<SalesforceQueryResult<DebugLevelDto>>() {}
            )
            val records = response.body?.records ?: emptyList()
            syncDebugLevelsToDatabase(records)
            records
        } catch (e: Exception) {
            println("Error querying DebugLevels: ${e.message}")
            emptyList()
        }
    }

    private fun syncDebugLevelsToDatabase(dtos: List<DebugLevelDto>) {
        dtos.forEach { dto ->
            val existing = debugLevelRepository.findBySfdcId(dto.id)
            val debugLevel = if (existing.isPresent) {
                existing.get().copy(
                    developerName = dto.developerName,
                    masterLabel = dto.masterLabel,
                    apexCode = dto.apexCode,
                    apexProfiling = dto.apexProfiling,
                    callout = dto.callout,
                    database = dto.database,
                    system = dto.system,
                    validation = dto.validation,
                    visualforce = dto.visualforce,
                    workflow = dto.workflow
                )
            } else {
                DebugLevel(
                    sfdcId = dto.id,
                    developerName = dto.developerName,
                    masterLabel = dto.masterLabel,
                    apexCode = dto.apexCode,
                    apexProfiling = dto.apexProfiling,
                    callout = dto.callout,
                    database = dto.database,
                    system = dto.system,
                    validation = dto.validation,
                    visualforce = dto.visualforce,
                    workflow = dto.workflow
                )
            }
            debugLevelRepository.save(debugLevel)
        }
    }

    @Cacheable(value = ["sf_metadata"], key = "'apex_classes_' + #limit + '_' + #offset", unless = "#result == null")
    fun getAllApexClasses(limit: Int = 10, offset: Int = 0): List<ApexClassDto> {
        val tokenResponse = authService.getAccessToken() ?: return emptyList()
        
        val baseUrl = tokenResponse.instanceUrl
        val query = "SELECT Id, Name, ApiVersion, Status, LengthWithoutComments, LastModifiedDate, LastModifiedBy.Name, CreatedDate, CreatedBy.Name FROM ApexClass WHERE Status = 'Active' ORDER BY Name ASC LIMIT $limit OFFSET $offset"
        
        val uri = UriComponentsBuilder.fromUriString("$baseUrl/services/data/$apiVersion/tooling/query")
            .queryParam("q", query)
            .build()
            .toUri()

        val headers = HttpHeaders()
        headers.setBearerAuth(tokenResponse.accessToken)
        
        val entity = HttpEntity<Unit>(headers)
        
        return try {
            val response: ResponseEntity<SalesforceQueryResult<ApexClassDto>> = restTemplate.exchange(
                uri,
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

    @Cacheable(value = ["sf_metadata"], key = "'apex_triggers_' + #limit + '_' + #offset", unless = "#result == null")
    fun getAllApexTriggers(limit: Int = 10, offset: Int = 0): List<ApexTriggerDto> {
        val tokenResponse = authService.getAccessToken() ?: return emptyList()
        
        val baseUrl = tokenResponse.instanceUrl
        val query = "SELECT Id, Name, TableEnumOrId, ApiVersion, Status, UsageBeforeInsert, UsageBeforeUpdate, UsageBeforeDelete, UsageAfterInsert, UsageAfterUpdate, UsageAfterDelete, LastModifiedDate, LastModifiedBy.Name, CreatedDate, CreatedBy.Name FROM ApexTrigger WHERE Status = 'Active' ORDER BY Name ASC LIMIT $limit OFFSET $offset"
        
        val uri = UriComponentsBuilder.fromUriString("$baseUrl/services/data/$apiVersion/tooling/query")
            .queryParam("q", query)
            .build()
            .toUri()

        val headers = HttpHeaders()
        headers.setBearerAuth(tokenResponse.accessToken)
        
        val entity = HttpEntity<Unit>(headers)
        
        return try {
            val response: ResponseEntity<SalesforceQueryResult<ApexTriggerDto>> = restTemplate.exchange(
                uri,
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

    fun searchClasses(name: String?): List<ApexClass> {
        val classes = if (name.isNullOrBlank()) {
            classRepository.findAll()
        } else {
            classRepository.findByNameContainingIgnoreCase(name)
        }
        
        if (classes.isEmpty() && name.isNullOrBlank()) {
            // Trigger background sync if DB is empty
            Thread { getAllApexClasses(limit = 100) }.start()
        }
        
        return classes
    }

    fun searchTriggers(name: String?): List<ApexTrigger> {
        val triggers = if (name.isNullOrBlank()) {
            triggerRepository.findAll()
        } else {
            triggerRepository.findByNameContainingIgnoreCase(name)
        }
        
        if (triggers.isEmpty() && name.isNullOrBlank()) {
            // Trigger background sync if DB is empty
            Thread { getAllApexTriggers(limit = 100) }.start()
        }
        
        return triggers
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
