package com.observability.sfdc.service

import com.observability.sfdc.domain.MetadataHistory
import com.observability.sfdc.dto.MetadataDiffDto
import com.observability.sfdc.repository.ApexClassRepository
import com.observability.sfdc.repository.ApexTriggerRepository
import com.observability.sfdc.repository.MetadataHistoryRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MetadataComparisonService(
    private val classRepository: ApexClassRepository,
    private val triggerRepository: ApexTriggerRepository,
    private val historyRepository: MetadataHistoryRepository
) {

    @Transactional(readOnly = true)
    fun compareMetadata(entityId: String, type: String): MetadataDiffDto {
        val latestBody = when (type) {
            "ApexClass" -> classRepository.findBySfdcId(entityId).orElse(null)?.body
            "ApexTrigger" -> triggerRepository.findBySfdcId(entityId).orElse(null)?.body
            else -> null
        } ?: throw IllegalArgumentException("Entity not found or has no body")

        val previousHistory = historyRepository.findTopBySfdcIdAndEntityTypeOrderByCreatedAtDesc(entityId, type)
        val previousBody = previousHistory?.body ?: ""

        return MetadataDiffDto(previousBody = previousBody, latestBody = latestBody)
    }
    
    @Transactional
    fun saveHistory(entityId: String, type: String, body: String) {
        historyRepository.save(MetadataHistory(sfdcId = entityId, entityType = type, body = body))
    }
}
