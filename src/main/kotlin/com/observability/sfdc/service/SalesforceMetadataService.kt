package com.observability.sfdc.service

import com.observability.sfdc.domain.ApexClass
import com.observability.sfdc.domain.ApexTrigger
import com.observability.sfdc.domain.DebugLevel
import com.observability.sfdc.dto.ApexClassDto
import com.observability.sfdc.dto.ApexTriggerDto
import com.observability.sfdc.dto.DebugLevelDto
import com.observability.sfdc.dto.MetadataDetailDto
import com.observability.sfdc.dto.SalesforceQueryResult
import com.observability.sfdc.dto.SalesforceSearchResponse
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

    @Cacheable(value = ["sf_metadata"], key = "'debug_levels_' + (#name ?: 'all') + '_' + #limit + '_' + #offset", unless = "#result == null")
    @Transactional
    fun getAllDebugLevels(name: String? = null, limit: Int = 10, offset: Int = 0): List<DebugLevelDto> {
        val tokenResponse = authService.getAccessToken() ?: return emptyList()
        
        val baseUrl = tokenResponse.instanceUrl!!
        var query = "SELECT Id, DeveloperName, MasterLabel, ApexCode, ApexProfiling, Callout, Database, System, Validation, Visualforce, Workflow FROM DebugLevel "
        
        if (!name.isNullOrBlank()) {
            val escapedName = name.replace("'", "\\'")
            query += "WHERE DeveloperName LIKE '%$escapedName%' OR MasterLabel LIKE '%$escapedName%' "
        }
        
        query += "LIMIT $limit OFFSET $offset"
        
        val uri = UriComponentsBuilder.fromUriString("$baseUrl/services/data/$apiVersion/tooling/query")
            .queryParam("q", query)
            .build()
            .toUri()

        val headers = HttpHeaders()
        headers.setBearerAuth(tokenResponse.accessToken!!)
        
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

    @Transactional
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

    @Cacheable(value = ["sf_metadata"], key = "'apex_classes_' + (#name ?: 'all') + '_' + #limit + '_' + #offset", unless = "#result == null")
    @Transactional
    fun getAllApexClasses(name: String? = null, limit: Int = 10, offset: Int = 0): List<ApexClassDto> {
        val tokenResponse = authService.getAccessToken() ?: return emptyList()
        
        val baseUrl = tokenResponse.instanceUrl!!
        var query = "SELECT Id, Name, ApiVersion, Status, LengthWithoutComments, LastModifiedDate, LastModifiedBy.Name, CreatedDate, CreatedBy.Name FROM ApexClass WHERE Status = 'Active' "
        
        if (!name.isNullOrBlank()) {
            val escapedName = name.replace("'", "\\'")
            query += "AND Name LIKE '%$escapedName%' "
        }
        
        query += "AND (NOT Name LIKE '%Test') AND (NOT Name LIKE 'Test%') AND (NOT Name LIKE '%Tests') AND (NOT Name LIKE '%Mock') AND (NOT Name LIKE '%Factory') ORDER BY Name ASC LIMIT $limit OFFSET $offset"
        
        val uri = UriComponentsBuilder.fromUriString("$baseUrl/services/data/$apiVersion/tooling/query")
            .queryParam("q", query)
            .build()
            .toUri()

        val headers = HttpHeaders()
        headers.setBearerAuth(tokenResponse.accessToken!!)
        
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

    @Cacheable(value = ["sf_metadata"], key = "'apex_triggers_' + (#name ?: 'all') + '_' + #limit + '_' + #offset", unless = "#result == null")
    @Transactional
    fun getAllApexTriggers(name: String? = null, limit: Int = 10, offset: Int = 0): List<ApexTriggerDto> {
        val tokenResponse = authService.getAccessToken() ?: return emptyList()
        
        val baseUrl = tokenResponse.instanceUrl!!
        var query = "SELECT Id, Name, TableEnumOrId, ApiVersion, Status, UsageBeforeInsert, UsageBeforeUpdate, UsageBeforeDelete, UsageAfterInsert, UsageAfterUpdate, UsageAfterDelete, UsageAfterUndelete, LastModifiedDate, LastModifiedBy.Name, CreatedDate, CreatedBy.Name FROM ApexTrigger WHERE Status = 'Active' "
        
        if (!name.isNullOrBlank()) {
            val escapedName = name.replace("'", "\\'")
            query += "AND Name LIKE '%$escapedName%' "
        }
        
        query += "ORDER BY Name ASC LIMIT $limit OFFSET $offset"
        
        val uri = UriComponentsBuilder.fromUriString("$baseUrl/services/data/$apiVersion/tooling/query")
            .queryParam("q", query)
            .build()
            .toUri()

        val headers = HttpHeaders()
        headers.setBearerAuth(tokenResponse.accessToken!!)
        
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

    fun searchClasses(name: String?, limit: Int = 10, offset: Int = 0): List<ApexClass> {
        val pageable = PageRequest.of(offset / limit, limit, Sort.by("name").ascending())
        
        // Sync with Salesforce if a name is provided or if the database is empty
        if (!name.isNullOrBlank()) {
            getAllApexClasses(name = name, limit = 200)
        } else if (classRepository.count() == 0L) {
            getAllApexClasses(limit = 200)
        }
        
        return if (name.isNullOrBlank()) {
            classRepository.findAllProjectedBy(pageable)
        } else {
            classRepository.findByNameContainingIgnoreCase(name, pageable)
        }
    }

    fun searchTriggers(name: String?, limit: Int = 10, offset: Int = 0): List<ApexTrigger> {
        val pageable = PageRequest.of(offset / limit, limit, Sort.by("name").ascending())
        
        // Sync with Salesforce if a name is provided or if the database is empty
        if (!name.isNullOrBlank()) {
            getAllApexTriggers(name = name, limit = 200)
        } else if (triggerRepository.count() == 0L) {
            getAllApexTriggers(limit = 200)
        }
        
        return if (name.isNullOrBlank()) {
            triggerRepository.findAllProjectedBy(pageable)
        } else {
            triggerRepository.findByNameContainingIgnoreCaseOrSobjectContainingIgnoreCase(name, name, pageable)
        }
    }

    fun searchDebugLevels(name: String?, limit: Int = 10, offset: Int = 0): List<DebugLevel> {
        val pageable = PageRequest.of(offset / limit, limit, Sort.by("developerName").ascending())
        
        // Sync with Salesforce if a name is provided or if the database is empty
        if (!name.isNullOrBlank()) {
            getAllDebugLevels(name = name, limit = 200)
        } else if (debugLevelRepository.count() == 0L) {
            getAllDebugLevels(limit = 200)
        }
        
        return if (name.isNullOrBlank()) {
            debugLevelRepository.findAllProjectedBy(pageable)
        } else {
            debugLevelRepository.findByDeveloperNameContainingIgnoreCaseOrMasterLabelContainingIgnoreCase(name, name, pageable)
        }
    }

    @Transactional
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

    @Transactional
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
                    usageAfterUndelete = dto.usageAfterUndelete,
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
                    usageAfterUndelete = dto.usageAfterUndelete,
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

    fun getMetadataDetail(id: String, type: String): MetadataDetailDto? {
        if (type != "ApexClass" && type != "ApexTrigger") {
            return null
        }
        
        val tokenResponse = authService.getAccessToken() ?: return null
        val baseUrl = tokenResponse.instanceUrl!!
        
        val fields = if (type == "ApexTrigger") {
            "Id, Name, TableEnumOrId, ApiVersion, Status, UsageBeforeInsert, UsageBeforeUpdate, UsageBeforeDelete, UsageAfterInsert, UsageAfterUpdate, UsageAfterDelete, UsageAfterUndelete, LastModifiedDate, LastModifiedBy.Name"
        } else {
            "Id, Name, ApiVersion, Status, LastModifiedDate, LastModifiedBy.Name"
        }
        
        val query = "SELECT $fields FROM $type WHERE Id = '$id'"
        val uri = UriComponentsBuilder.fromUriString("$baseUrl/services/data/$apiVersion/tooling/query")
            .queryParam("q", query)
            .build()
            .toUri()

        val headers = HttpHeaders()
        headers.setBearerAuth(tokenResponse.accessToken!!)
        val entity = HttpEntity<Unit>(headers)

        return try {
            if (type == "ApexTrigger") {
                val response = restTemplate.exchange(uri, HttpMethod.GET, entity, object : ParameterizedTypeReference<SalesforceQueryResult<ApexTriggerDto>>() {}).body
                val trigger = response?.records?.firstOrNull() ?: return null
                val testClasses = findRelatedTestClasses(trigger.name!!)
                
                MetadataDetailDto(
                    id = trigger.id,
                    name = trigger.name,
                    type = "ApexTrigger",
                    apiVersion = trigger.apiVersion,
                    status = trigger.status,
                    lastModifiedDate = trigger.lastModifiedDate,
                    lastModifiedByName = trigger.lastModifiedBy?.name,
                    targetObject = trigger.tableEnumOrId,
                    triggerEvents = mapTriggerEvents(trigger),
                    testClasses = testClasses
                )
            } else {
                val response = restTemplate.exchange(uri, HttpMethod.GET, entity, object : ParameterizedTypeReference<SalesforceQueryResult<ApexClassDto>>() {}).body
                val apexClass = response?.records?.firstOrNull() ?: return null
                val testClasses = findRelatedTestClasses(apexClass.name!!)

                MetadataDetailDto(
                    id = apexClass.id,
                    name = apexClass.name,
                    type = "ApexClass",
                    apiVersion = apexClass.apiVersion,
                    status = apexClass.status,
                    lastModifiedDate = apexClass.lastModifiedDate,
                    lastModifiedByName = apexClass.lastModifiedBy?.name,
                    testClasses = testClasses
                )
            }
        } catch (e: Exception) {
            logger.error("Error fetching metadata detail for $id ($type): ${e.message}")
            null
        }
    }

    private fun findRelatedTestClasses(name: String): List<ApexClassDto> {
        val tokenResponse = authService.getAccessToken() ?: return emptyList()
        val baseUrl = tokenResponse.instanceUrl!!
        
        // SOSL Query to find classes that mention the metadata in their body
        val sosl = "FIND {$name} IN ALL FIELDS RETURNING ApexClass (Id, Name, ApiVersion, Status, LastModifiedDate, LastModifiedBy.Name WHERE Name != '$name' AND Status = 'Active')"
        
        val uri = UriComponentsBuilder.fromUriString("$baseUrl/services/data/$apiVersion/tooling/search")
            .queryParam("q", sosl)
            .build()
            .toUri()

        val headers = HttpHeaders()
        headers.setBearerAuth(tokenResponse.accessToken!!)
        val entity = HttpEntity<Unit>(headers)

        return try {
            val response = restTemplate.exchange(uri, HttpMethod.GET, entity, object : ParameterizedTypeReference<SalesforceSearchResponse<ApexClassDto>>() {}).body
            response?.searchRecords ?: emptyList()
        } catch (e: Exception) {
            logger.error("Error searching for related test classes for $name: ${e.message}")
            emptyList()
        }
    }

    private fun mapTriggerEvents(dto: ApexTriggerDto): List<String> {
        val events = mutableListOf<String>()
        if (dto.usageBeforeInsert == true) events.add("Before Insert")
        if (dto.usageBeforeUpdate == true) events.add("Before Update")
        if (dto.usageBeforeDelete == true) events.add("Before Delete")
        if (dto.usageAfterInsert == true) events.add("After Insert")
        if (dto.usageAfterUpdate == true) events.add("After Update")
        if (dto.usageAfterDelete == true) events.add("After Delete")
        if (dto.usageAfterUndelete == true) events.add("After Undelete")
        return events
    }
}
