package com.observability.sfdc.util

import org.springframework.boot.health.contributor.Health
import org.springframework.boot.health.contributor.HealthIndicator as SpringHealthIndicator
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.stereotype.Component
import javax.sql.DataSource

@Component("appHealthIndicator")
class AppHealthIndicator(
    private val dataSource: DataSource,
    private val redisConnectionFactory: RedisConnectionFactory,
    private val authStatus: SalesforceAuthStatus
) : SpringHealthIndicator {

    override fun health(): Health {
        val builder = Health.up()

        // Check DB
        try {
            dataSource.connection.use { conn ->
                val rs = conn.createStatement().executeQuery("SELECT 1")
                if (rs.next()) builder.withDetail("db", "UP")
            }
        } catch (e: Exception) {
            builder.down().withDetail("db", "DOWN: ${e.message}")
        }

        // Check Redis
        try {
            redisConnectionFactory.connection.ping()
            builder.withDetail("redis", "UP")
        } catch (e: Exception) {
            builder.down().withDetail("redis", "DOWN: ${e.message}")
        }

        // Check Salesforce
        if (authStatus.lastSuccess) {
            builder.withDetail("Salesforce", "UP")
                .withDetail("lastCheckedAt", "${authStatus.lastCheckedAt}")
                .build()
        } else {
            builder.down()
                .withDetail("Salesforce", "DOWN: ${authStatus.lastError}")
                .withDetail("lastCheckedAt", "${authStatus.lastCheckedAt}")
                .build()
        }

        return builder.build()
    }
}
