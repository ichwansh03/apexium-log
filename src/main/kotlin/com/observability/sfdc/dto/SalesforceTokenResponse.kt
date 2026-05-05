package com.observability.sfdc.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class SalesforceTokenResponse(
    @JsonProperty("access_token")
    val accessToken: String,
    
    @JsonProperty("instance_url")
    val instanceUrl: String,
    
    @JsonProperty("id")
    val id: String,
    
    @JsonProperty("token_type")
    val tokenType: String,
    
    @JsonProperty("issued_at")
    val issuedAt: String,
    
    @JsonProperty("signature")
    val signature: String
)
