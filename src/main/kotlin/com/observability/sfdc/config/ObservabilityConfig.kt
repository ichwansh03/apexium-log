package com.observability.sfdc.config

import io.micrometer.core.instrument.MeterRegistry
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.micrometer.metrics.autoconfigure.MeterRegistryCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Adds common tags to every Micrometer meter so Prometheus series can be
 * filtered by application and deployment environment.
 */
@Configuration
class ObservabilityConfig(
    @Value($$"${spring.application.name:apexium-log}") private val applicationName: String,
    @Value($$"${observability.environment:local}") private val environment: String
) {

    @Bean
    fun commonTagsCustomizer(): MeterRegistryCustomizer<MeterRegistry> =
        MeterRegistryCustomizer { registry ->
            registry.config().commonTags(
                "application", applicationName,
                "environment", environment
            )
        }
}
