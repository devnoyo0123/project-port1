package com.portone.payment.client

import mu.KotlinLogging
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {}

@Component
class PortOneTokenManager(
    private val cacheManager: CacheManager
) {
    private val TOKEN_CACHE_NAME = "portOneTokenCache"
    private val TOKEN_CACHE_KEY_PREFIX = "portOneAccessToken:"
    private val EXPIRES_AT_CACHE_KEY_PREFIX = "portOneTokenExpiresAt:"
    private val NOW_CACHE_KEY_PREFIX = "portOneTokenNow:"

    /**
     * 현재 캐시에 저장된 액세스 토큰을 반환합니다.
     * 토큰이 없으면 null을 반환합니다.
     */
    fun getCurrentToken(impKey: String): String? {
        val cache = cacheManager.getCache(TOKEN_CACHE_NAME)
        return cache?.get(TOKEN_CACHE_KEY_PREFIX + impKey, String::class.java)
    }

    /**
     * 토큰을 캐시에 저장합니다.
     */
    fun setToken(impKey: String, token: String, expiresAt: Long, now: Long) {
        val cache = cacheManager.getCache(TOKEN_CACHE_NAME)
        if (cache != null) {
            // API에서 제공한 값을 그대로 사용
            cache.put(TOKEN_CACHE_KEY_PREFIX + impKey, token)
            cache.put(EXPIRES_AT_CACHE_KEY_PREFIX + impKey, expiresAt)
            cache.put(NOW_CACHE_KEY_PREFIX + impKey, now)
            
            // 만료 시간과 현재 시간의 차이를 계산 (둘 다 초 단위)
            val timeToLiveSeconds = (expiresAt - now).toInt()
            
            logger.info { "포트원 액세스 토큰을 캐시에 저장: imp-key: ${maskImpKey(impKey)}, 토큰 유효 시간 ${timeToLiveSeconds}초" }
        } else {
            logger.error { "캐시를 찾을 수 없습니다: $TOKEN_CACHE_NAME" }
        }
    }

    /**
     * 토큰을 무효화합니다.
     */
    fun invalidateToken(impKey: String) {
        val cache = cacheManager.getCache(TOKEN_CACHE_NAME)
        cache?.evict(TOKEN_CACHE_KEY_PREFIX + impKey)
        cache?.evict(EXPIRES_AT_CACHE_KEY_PREFIX + impKey)
        cache?.evict(NOW_CACHE_KEY_PREFIX + impKey)
        logger.info { "포트원 액세스 토큰 캐시에서 제거 완료: imp-key: ${maskImpKey(impKey)}" }
    }

    /**
     * 토큰 만료 시간을 확인합니다.
     */
    fun getTokenExpiresAt(impKey: String): Long? {
        val cache = cacheManager.getCache(TOKEN_CACHE_NAME)
        val value = cache?.get(EXPIRES_AT_CACHE_KEY_PREFIX + impKey)?.get()

        return when (value) {
            is Long -> value
            is Int -> value.toLong()
            is Number -> value.toLong()
            null -> null
            else -> {
                logger.warn { "캐시에서 가져온 만료 시간이 예상 타입이 아닙니다: ${value.javaClass.name}, imp-key: ${maskImpKey(impKey)}" }
                null
            }
        }
    }

    /**
     * 토큰 생성 시간을 확인합니다.
     */
    fun getTokenNow(impKey: String): Long? {
        val cache = cacheManager.getCache(TOKEN_CACHE_NAME)
        val value = cache?.get(NOW_CACHE_KEY_PREFIX + impKey)?.get()

        return when (value) {
            is Long -> value
            is Int -> value.toLong()
            is Number -> value.toLong()
            null -> null
            else -> {
                logger.warn { "캐시에서 가져온 생성 시간이 예상 타입이 아닙니다: ${value.javaClass.name}, imp-key: ${maskImpKey(impKey)}" }
                null
            }
        }
    }

    /**
     * 토큰이 유효한지 확인합니다. (현재 시간 기준으로 1분 이상 남았는지)
     */
    fun isTokenValid(impKey: String): Boolean {
        val token = getCurrentToken(impKey)
        val expiresAt = getTokenExpiresAt(impKey)
        val now = getTokenNow(impKey)

        if (token == null || expiresAt == null || now == null) {
            return false
        }

        // 현재 시간 대신 API에서 받은 now 값을 기준으로 계산
        // now는 초 단위, expiresAt도 초 단위이므로 밀리초로 변환하여 계산
        val elapsedMillis = System.currentTimeMillis() - now * 1000
        val serverCurrentTimeMillis = now * 1000 + elapsedMillis

        // 1분(60000밀리초) 이상 남았는지 확인
        return expiresAt * 1000 - serverCurrentTimeMillis > 60000
    }
    
    /**
     * 로깅 시 imp-key를 마스킹하여 보안을 유지합니다.
     */
    private fun maskImpKey(impKey: String): String {
        return if (impKey.length > 8) {
            impKey.substring(0, 4) + "..." + impKey.substring(impKey.length - 4)
        } else {
            "****"
        }
    }
}
