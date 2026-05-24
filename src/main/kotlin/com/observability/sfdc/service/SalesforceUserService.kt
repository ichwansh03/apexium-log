package com.observability.sfdc.service

import com.observability.sfdc.domain.User
import com.observability.sfdc.dto.SalesforceQueryResult
import com.observability.sfdc.dto.SalesforceUserDto
import com.observability.sfdc.repository.UserRepository
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
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

@Service
class SalesforceUserService(
    private val authService: SalesforceAuthService,
    private val userRepository: UserRepository,
    @Value($$"${salesforce.api-version}") private val apiVersion: String
) {
    private val restTemplate = RestTemplate()

    @Cacheable(value = ["sf_users"], key = "'all_users_' + #limit + '_' + #offset", unless = "#result == null")
    fun getAllUsers(limit: Int = 10, offset: Int = 0): List<SalesforceUserDto> {
        val tokenResponse = authService.getAccessToken() ?: return emptyList()
        
        val baseUrl = tokenResponse.instanceUrl
        val query = "SELECT Id, Name, Username, Email, Profile.Name, IsActive, Entity__c FROM User WHERE (IsActive = TRUE AND Entity__c = 'AMFS') OR Name = 'Automated Process' ORDER BY Name ASC LIMIT $limit OFFSET $offset"
        
        val uri = UriComponentsBuilder.fromUriString("$baseUrl/services/data/$apiVersion/query")
            .queryParam("q", query)
            .build()
            .toUri()

        val headers = HttpHeaders()
        headers.setBearerAuth(tokenResponse.accessToken)
        
        val entity = HttpEntity<Unit>(headers)
        
        return try {
            val response: ResponseEntity<SalesforceQueryResult<SalesforceUserDto>> = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                entity,
                object : ParameterizedTypeReference<SalesforceQueryResult<SalesforceUserDto>>() {}
            )
            val records = response.body?.records ?: emptyList()
            
            // Sync to database asynchronously or in-line (doing in-line for simplicity here)
            syncUsersToDatabase(records)
            
            records
        } catch (e: Exception) {
            println("Error querying Salesforce Users: ${e.message}")
            emptyList()
        }
    }

    fun searchUsers(name: String?, limit: Int = 10, offset: Int = 0): List<User> {
        val pageable = PageRequest.of(offset / limit, limit, Sort.by("name").ascending())
        val users = if (name.isNullOrBlank()) {
            userRepository.findAllProjectedBy(pageable)
        } else {
            userRepository.findByNameContainingIgnoreCase(name, pageable)
        }
        
        if (users.isEmpty() && name.isNullOrBlank()) {
            // Trigger background sync if DB is empty
            Thread { getAllUsers(limit = 100) }.start()
        }
        
        return users
    }

    private fun syncUsersToDatabase(dtos: List<SalesforceUserDto>) {
        dtos.forEach { dto ->
            val existing = userRepository.findBySfdcId(dto.id)
            val user = if (existing.isPresent) {
                existing.get().copy(
                    name = dto.name,
                    username = dto.username,
                    email = dto.email,
                    profileName = dto.profile?.name,
                    isActive = dto.isActive,
                    entity = dto.entity
                )
            } else {
                User(
                    sfdcId = dto.id,
                    name = dto.name,
                    username = dto.username,
                    email = dto.email,
                    profileName = dto.profile?.name,
                    isActive = dto.isActive,
                    entity = dto.entity
                )
            }
            userRepository.save(user)
        }
    }
}
