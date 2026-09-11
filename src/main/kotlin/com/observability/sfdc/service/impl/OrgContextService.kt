package com.observability.sfdc.service.impl

import com.observability.sfdc.dto.SalesforceTokenResponse
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.ScanOptions
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service

@Service
class OrgContextService(
    private val redisTemplate: StringRedisTemplate
) {
    private val logger = LoggerFactory.getLogger(OrgContextService::class.java)
    private val ACTIVE_ORG_KEY = "system:active_org"
    private val CACHE_PREFIXES = listOf("sf_tokens:", "sf_metadata:", "sf_users:")

    @Volatile
    private var currentOrgId: String? = null

    fun getActiveOrgId(): String {
        return currentOrgId ?: redisTemplate.opsForValue().get(ACTIVE_ORG_KEY) ?: "UNKNOWN_ORG"
    }

    fun ensureActiveOrg(tokenResponse: SalesforceTokenResponse) {
        val orgId = extractOrgId(tokenResponse)
        if (orgId == null) {
            logger.warn("Could not extract orgId from token response id field")
            return
        }

        val previousOrgId = currentOrgId ?: redisTemplate.opsForValue().get(ACTIVE_ORG_KEY)

        currentOrgId = orgId
        redisTemplate.opsForValue().set(ACTIVE_ORG_KEY, orgId)

        if (previousOrgId != null && previousOrgId != orgId) {
            logger.warn("Org switch detected: $previousOrgId -> $orgId. Flushing stale caches.")
            flushAllSalesforceCaches()
        } else if (previousOrgId == null) {
            logger.info("Active org set: $orgId")
        }
    }

    fun flushAllSalesforceCaches() {
        var totalDeleted = 0L
        for (prefix in CACHE_PREFIXES) {
            val keysToDelete = scanKeys("$prefix*")
            if (keysToDelete.isNotEmpty()) {
                keysToDelete.chunked(100).forEach { batch ->
                    redisTemplate.delete(batch)
                }
                totalDeleted += keysToDelete.size
            }
        }
        logger.info("Flushed $totalDeleted cache keys across ${CACHE_PREFIXES.size} caches.")
    }

    private fun scanKeys(pattern: String): List<String> {
        val keys = mutableListOf<String>()
        val scanOptions = ScanOptions.scanOptions().match(pattern).count(200).build()

        redisTemplate.executeWithStickyConnection { connection ->
            val cursor = connection.scan(scanOptions)
            while (cursor.hasNext()) {
                keys.add(String(cursor.next()))
            }
            cursor.close()
            null
        }
        return keys
    }

    companion object {
        fun extractOrgId(tokenResponse: SalesforceTokenResponse): String? {
            val idUrl = tokenResponse.id ?: return null

            return try {
                val path = idUrl.substringAfter("/id/")
                val segments = path.split("/")
                if (segments.isNotEmpty()) segments[0] else null
            } catch (_: Exception) {
                null
            }
        }
    }
}