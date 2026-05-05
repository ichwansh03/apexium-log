package com.observability.sfdc.service

import com.observability.sfdc.dto.SalesforceTokenResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate

@Service
class SalesforceAuthService(
    @Value("\${salesforce.login-url}") private val loginUrl: String,
    @Value("\${salesforce.client-id}") private val clientId: String,
    @Value("\${salesforce.client-secret}") private val clientSecret: String,
    @Value("\${salesforce.grant-type}") private val grantType: String
) {
    private val restTemplate = RestTemplate()

    fun getAccessToken(): SalesforceTokenResponse? {
        val url = "$loginUrl/services/oauth2/token"
        
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED

        val map = LinkedMultiValueMap<String, String>()
        map.add("grant_type", grantType)
        map.add("client_id", clientId)
        map.add("client_secret", clientSecret)

        val request = HttpEntity(map, headers)

        return try {
            restTemplate.postForObject(url, request, SalesforceTokenResponse::class.java)
        } catch (e: Exception) {
            println("Error authenticating with Salesforce: ${e.message}")
            null
        }
    }
}
