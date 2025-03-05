package com.portone.payment.client.config

import com.portone.payment.client.PortOneTokenManager
import com.portone.payment.client.PortOneTokenService
import mu.KotlinLogging
import org.springframework.http.HttpRequest
import org.springframework.http.HttpStatus
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.ClientHttpResponse
import org.springframework.web.client.HttpClientErrorException

private val logger = KotlinLogging.logger {}

class PortOneAuthInterceptor(
    private val tokenManager: PortOneTokenManager,
    private val tokenService: PortOneTokenService,
    private val impKey: String  // 생성자를 통해 impKey 전달받음
) : ClientHttpRequestInterceptor {

    override fun intercept(
        request: HttpRequest,
        body: ByteArray,
        execution: ClientHttpRequestExecution
    ): ClientHttpResponse {
        tokenService.ensureValidToken()

        // 토큰 헤더 추가
        val token = tokenManager.getCurrentToken(impKey)
        if (token != null) {
            request.headers.setBearerAuth(token)
        }

        return try {
            // 요청 실행
            execution.execute(request, body)
        } catch (ex: HttpClientErrorException) {
            // 401 에러 시 토큰 갱신 후 재시도
            if (ex.statusCode == HttpStatus.UNAUTHORIZED) {
                logger.info { "인증 실패 (401), 토큰 갱신 후 재시도합니다." }
                tokenManager.invalidateToken(impKey)
                tokenService.ensureValidToken()

                // 토큰 재설정
                if (tokenManager.getCurrentToken(impKey) != null) {
                    request.headers.setBearerAuth(tokenManager.getCurrentToken(impKey)!!)
                }

                // 요청 재시도
                return execution.execute(request, body)
            }
            throw ex
        }
    }
}
