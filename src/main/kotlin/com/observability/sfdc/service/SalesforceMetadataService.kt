package com.observability.sfdc.service

import com.observability.sfdc.domain.ApexClass
import com.observability.sfdc.domain.ApexTrigger
import com.observability.sfdc.domain.DebugLevel
import com.observability.sfdc.dto.ApexClassDto
import com.observability.sfdc.dto.ApexTriggerDto
import com.observability.sfdc.dto.DebugLevelDto
import com.observability.sfdc.dto.SalesforceQueryResult
import com.observability.sfdc.repository.ApexClassRepository
import com.observability.sfdc.repository.ApexTriggerRepository
import com.observability.sfdc.repository.DebugLevelRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.Cacheable
import org.springframework.core.ParameterizedTypeReference
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

import org.slf4j.LoggerFactory

@Service
class SalesforceMetadataService(
    private val authService: SalesforceAuthService,
    private val classRepository: ApexClassRepository,
    private val triggerRepository: ApexTriggerRepository,
    private val debugLevelRepository: DebugLevelRepository,
    @Value($$"${salesforce.api-version}") private val apiVersion: String
) {
    private val restTemplate = RestTemplate()
    private val logger = LoggerFactory.getLogger(SalesforceMetadataService::class.java)

    @Cacheable(value = ["sf_metadata"], key = "'debug_levels_' + #limit + '_' + #offset", unless = "#result == null")
    @Transactional
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
            logger.error("Error querying DebugLevels: ${e.message}", e)
            emptyList()
        }
    }

    private fun syncDebugLevelsToDatabase(dtos: List<DebugLevelDto>) {
        // Filter to unique IDs in case SF returns duplicates (unlikely but safe)
        dtos.distinctBy { it.id }.forEach { dto ->
            val existing = debugLevelRepository.findBySfdcId(dto.id)
            if (existing.isPresent) {
                val current = existing.get()
                logger.debug("Updating existing DebugLevel: {} ({})", dto.developerName, dto.id)
                val updated = current.copy(
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
                debugLevelRepository.save(updated)
            } else {
                logger.debug("Creating new DebugLevel: {} ({})", dto.developerName, dto.id)
                val debugLevel = DebugLevel(
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
                try {
                    debugLevelRepository.save(debugLevel)
                } catch (e: Exception) {
                    logger.error("Failed to save new DebugLevel {}: {}", dto.id, e.message)
                }
            }
        }
    }

    @Cacheable(value = ["sf_metadata"], key = "'apex_classes_' + #limit + '_' + #offset", unless = "#result == null")
    @Transactional
    fun getAllApexClasses(limit: Int = 10, offset: Int = 0): List<ApexClassDto> {
        val tokenResponse = authService.getAccessToken() ?: return emptyList()
        
        val baseUrl = tokenResponse.instanceUrl
        val query = "SELECT Id, Name, ApiVersion, Status, LengthWithoutComments, LastModifiedDate, LastModifiedBy.Name, CreatedDate, CreatedBy.Name FROM ApexClass WHERE Status = 'Active' AND (NOT Name LIKE '%Test') AND (NOT Name LIKE 'Test%') AND (NOT Name LIKE '%Tests') AND (NOT Name LIKE '%Mock') AND (NOT Name LIKE '%Factory') ORDER BY Name ASC LIMIT $limit OFFSET $offset"
        
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
    @Transactional
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

    @Cacheable(value = ["sf_metadata"], key = "'search_classes_' + (#name ?: 'null') + '_' + #limit + '_' + #offset", unless = "#result == null || #result.isEmpty()")
    fun searchClasses(name: String?, limit: Int = 10, offset: Int = 0): List<ApexClass> {
        val pageable = PageRequest.of(offset / limit, limit, Sort.by("name").ascending())
        var classes = if (name.isNullOrBlank()) {
            classRepository.findAllProjectedBy(pageable)
        } else {
            classRepository.findByNameContainingIgnoreCase(name, pageable)
        }
        
        if (classes.isEmpty() && name.isNullOrBlank()) {
            // SYNC fetch if DB is empty on first load
            val sfClasses = getAllApexClasses(limit = 100)
            if (sfClasses.isNotEmpty()) {
                classes = classRepository.findAllProjectedBy(pageable)
            }
        }
        
        return classes
    }

    @Cacheable(value = ["sf_metadata"], key = "'search_triggers_' + (#name ?: 'null') + '_' + #limit + '_' + #offset", unless = "#result == null || #result.isEmpty()")
    fun searchTriggers(name: String?, limit: Int = 10, offset: Int = 0): List<ApexTrigger> {
        val pageable = PageRequest.of(offset / limit, limit, Sort.by("name").ascending())
        var triggers = if (name.isNullOrBlank()) {
            triggerRepository.findAllProjectedBy(pageable)
        } else {
            triggerRepository.findByNameContainingIgnoreCaseOrSobjectContainingIgnoreCase(name, name, pageable)
        }
        
        if (triggers.isEmpty() && name.isNullOrBlank()) {
            // SYNC fetch if DB is empty on first load
            val sfTriggers = getAllApexTriggers(limit = 100)
            if (sfTriggers.isNotEmpty()) {
                triggers = triggerRepository.findAllProjectedBy(pageable)
            }
        }
        
        return triggers
    }

    @Cacheable(value = ["sf_metadata"], key = "'search_debug_levels_' + (#name ?: 'null') + '_' + #limit + '_' + #offset", unless = "#result == null || #result.isEmpty()")
    fun searchDebugLevels(name: String?, limit: Int = 10, offset: Int = 0): List<DebugLevel> {
        val pageable = PageRequest.of(offset / limit, limit, Sort.by("developerName").ascending())
        var levels = if (name.isNullOrBlank()) {
            debugLevelRepository.findAllProjectedBy(pageable)
        } else {
            debugLevelRepository.findByDeveloperNameContainingIgnoreCaseOrMasterLabelContainingIgnoreCase(name, name, pageable)
        }
        
        if (levels.isEmpty() && name.isNullOrBlank()) {
            // SYNC fetch if DB is empty on first load
            val sfLevels = getAllDebugLevels(limit = 100)
            if (sfLevels.isNotEmpty()) {
                levels = debugLevelRepository.findAllProjectedBy(pageable)
            }
        }
        
        return levels
    }

    private fun syncClassesToDatabase(dtos: List<ApexClassDto>) {
        dtos.distinctBy { it.id }.forEach { dto ->
            val existing = classRepository.findBySfdcId(dto.id)
            if (existing.isPresent) {
                val current = existing.get()
                val updated = current.copy(
                    name = dto.name,
                    apiVersion = dto.apiVersion,
                    status = dto.status,
                    lengthWithoutComments = dto.lengthWithoutComments,
                    lastModifiedDate = dto.lastModifiedDate,
                    lastModifiedByName = dto.lastModifiedBy?.name,
                    createdDate = dto.createdDate,
                    createdByName = dto.createdBy?.name
                )
                classRepository.save(updated)
            } else {
                val apexClass = ApexClass(
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
                try {
                    classRepository.save(apexClass)
                } catch (e: Exception) {
                    logger.error("Failed to save new ApexClass {}: {}", dto.id, e.message)
                }
            }
        }
    }

    private fun syncTriggersToDatabase(dtos: List<ApexTriggerDto>) {
        dtos.distinctBy { it.id }.forEach { dto ->
            val existing = triggerRepository.findBySfdcId(dto.id)
            if (existing.isPresent) {
                val current = existing.get()
                val updated = current.copy(
                    name = dto.name,
                    sobject = dto.tableEnumOrId,
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
                triggerRepository.save(updated)
            } else {
                val trigger = ApexTrigger(
                    sfdcId = dto.id,
                    name = dto.name,
                    sobject = dto.tableEnumOrId,
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
                try {
                    triggerRepository.save(trigger)
                } catch (e: Exception) {
                    logger.error("Failed to save new ApexTrigger {}: {}", dto.id, e.message)
                }
            }
        }
    }
}
