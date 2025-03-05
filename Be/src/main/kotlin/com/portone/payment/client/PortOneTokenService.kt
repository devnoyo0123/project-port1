package com.portone.payment.client

import com.portone.payment.api.exception.PortOneApiException
import com.portone.payment.client.model.response.TokenResponse
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import org.springframework.web.client.toEntity

private val logger = KotlinLogging.logger {  }

@Component
class PortOneTokenService(
    @Qualifier("tokenRestClient")
    private val tokenRestClient: RestClient,
    private val tokenManager: PortOneTokenManager,
    @Value("\${portone.api.base-url}") private val baseUrl: String,
    @Value("\${portone.api.imp-key}") private val impKey: String,
    @Value("\${portone.api.imp-secret}") private val impSecret: String
) {

    /**
     * 유효한 토큰이 있는지 확인하고, 없으면 새로 발급합니다.
     * 기본 imp-key를 사용합니다.
     */
    fun ensureValidToken() {
        ensureValidToken(impKey, impSecret)
    }

    /**
     * 유효한 토큰이 있는지 확인하고, 없으면 새로 발급합니다.
     * 지정된 imp-key와 imp-secret을 사용합니다.
     */
    fun ensureValidToken(impKey: String, impSecret: String) {
        if (!tokenManager.isTokenValid(impKey)) {
            var retryCount = 0
            val maxRetries = 5
            var lastException: Exception? = null
            val baseDelayMs = 1000L // 기본 지연 시간 1초

            while (retryCount < maxRetries) {
                try {
                    requestToken(impKey, impSecret)
                    return // 성공하면 즉시 반환
                } catch (e: Exception) {
                    lastException = e
                    logger.warn(e) { "토큰 갱신 중 오류 발생 (시도: ${retryCount + 1}/$maxRetries)" }
                    retryCount++

                    if (retryCount < maxRetries) {
                        // 지수 백오프: 2^retryCount * baseDelayMs (약간의 랜덤성 추가)
                        val exponentialDelay = (Math.pow(2.0, retryCount.toDouble()) * baseDelayMs).toLong()
                        val jitter = (Math.random() * 0.1 * exponentialDelay).toLong() // 10% 이내의 랜덤 지터
                        val delayMs = exponentialDelay + jitter

                        logger.info { "재시도 대기 중... ${delayMs}ms 후 재시도합니다." }
                        Thread.sleep(delayMs)
                    }
                }
            }

            // 모든 재시도 실패 후 마지막 예외 로깅 및 던지기
            logger.error(lastException) { "토큰 갱신 최대 재시도 횟수($maxRetries) 초과" }
            throw lastException ?: PortOneApiException(
                httpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
                message = "토큰 갱신 실패",
                errorCode = "TOKEN_RETRY_FAILED"
            )
        }
    }


    /**
     * 토큰을 발급받습니다.
     * 지정된 imp-key와 imp-secret을 사용합니다.
     */
    fun requestToken(impKey: String, impSecret: String): TokenResponse {
        val responseEntity = tokenRestClient
            .post()
            .uri("$baseUrl/users/getToken")
            .contentType(MediaType.APPLICATION_JSON)
            .body(mapOf(
                "imp_key" to impKey,
                "imp_secret" to impSecret
            ))
            .retrieve()
            .toEntity<TokenResponse>()

        val response = responseEntity.body

        if (response != null) {
            tokenManager.setToken(
                impKey,
                response.response.access_token,
                response.response.expired_at,
                response.response.now
            )
            return response
        } else {
            throw PortOneApiException(
                httpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
                message = "토큰 응답이 null입니다",
                errorCode = "TOKEN_RESPONSE_NULL"
            )
        }
    }
}
