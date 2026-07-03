package com.observability.sfdc.dto

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class ApexCodeCoverageDto(
    @JsonAlias("ApexClassOrTriggerId")
    val apexClassOrTriggerId: String,
    
    @JsonAlias("NumLinesCovered")
    val numLinesCovered: Int = 0,
    
    @JsonAlias("NumLinesUncovered")
    val numLinesUncovered: Int = 0
) {
    val coveragePercentage: Double
        get() {
            val totalLines = numLinesCovered + numLinesUncovered
            if (totalLines == 0) return 0.0
            return (numLinesCovered.toDouble() / totalLines.toDouble()) * 100.0
        }
}
