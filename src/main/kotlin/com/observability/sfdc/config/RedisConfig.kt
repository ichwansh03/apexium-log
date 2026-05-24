package com.observability.sfdc.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.CacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer
import tools.jackson.databind.ObjectMapper
import java.time.Duration

@Configuration
class RedisConfig(
    @Value($$"${cache.ttl.token:3600}") private val tokenTtl: Long,
    @Value($$"${cache.ttl.metadata:3600}") private val metadataTtl: Long,
    @Value($$"${cache.ttl.logs:86400}") private val logsTtl: Long
) {

    @Bean
    fun cacheManager(connectionFactory: RedisConnectionFactory, objectMapper: ObjectMapper): CacheManager {
        val serializer = GenericJacksonJsonRedisSerializer(objectMapper)

        val defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(StringRedisSerializer()))
            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer))
            .entryTtl(Duration.ofSeconds(metadataTtl)) // Default TTL

        val cacheConfigs = mutableMapOf<String, RedisCacheConfiguration>()
        
        // Custom TTL for specific caches
        cacheConfigs["sf_tokens"] = defaultConfig.entryTtl(Duration.ofSeconds(tokenTtl))
        cacheConfigs["sf_logs_body"] = defaultConfig.entryTtl(Duration.ofSeconds(logsTtl))
        cacheConfigs["sf_metadata"] = defaultConfig.entryTtl(Duration.ofSeconds(metadataTtl))
        cacheConfigs["sf_users"] = defaultConfig.entryTtl(Duration.ofSeconds(metadataTtl))

        return RedisCacheManager.builder(connectionFactory)
            .cacheDefaults(defaultConfig)
            .withInitialCacheConfigurations(cacheConfigs)
            .build()
    }
}
