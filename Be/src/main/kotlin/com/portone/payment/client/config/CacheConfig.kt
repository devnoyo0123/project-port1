package com.portone.payment.client.config

import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit

@Configuration
@EnableCaching
class CacheConfig {

    @Bean
    fun cacheManager(): CacheManager {
        val caffeine = Caffeine.newBuilder()
            .expireAfterWrite(1, TimeUnit.HOURS) // 기본 만료 시간
            .maximumSize(100)

        val cacheManager = CaffeineCacheManager()
        cacheManager.setCaffeine(caffeine)
        cacheManager.setCacheNames(setOf("portOneTokenCache"))

        return cacheManager
    }
}
