package com.observability.sfdc.dto

import com.fasterxml.jackson.annotation.JsonProperty

@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
data class SalesforceTokenResponse(
    @JsonProperty("access_token")
    val accessToken: String? = null,
    
    @JsonProperty("instance_url")
    val instanceUrl: String? = null,
    
    @JsonProperty("id")
    val id: String? = null,
    
    @JsonProperty("token_type")
    val tokenType: String? = null,
    
    @JsonProperty("issued_at")
    val issuedAt: String? = null,
    
    @JsonProperty("signature")
    val signature: String? = null
)
