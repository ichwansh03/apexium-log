package com.observability.sfdc.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class ApexCodeCoverageDto(
    @JsonProperty("ApexClassOrTriggerId")
    val apexClassOrTriggerId: String,
    
    @JsonProperty("NumLinesCovered")
    val numLinesCovered: Int = 0,
    
    @JsonProperty("NumLinesUncovered")
    val numLinesUncovered: Int = 0
) {
    val coveragePercentage: Double
        get() {
            val totalLines = numLinesCovered + numLinesUncovered
            if (totalLines == 0) return 0.0
            return (numLinesCovered.toDouble() / totalLines.toDouble()) * 100.0
        }
}
