package com.portone.payment.client

import com.portone.payment.api.exception.PortOneApiException
import com.portone.payment.client.model.request.PaymentRequest
import com.portone.payment.client.model.response.PaymentResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestClient
import org.springframework.web.client.toEntity

@Component
class PortOneApiClient(
    private val restClient: RestClient,
    private val tokenManager: PortOneTokenManager,
    @Value("\${portone.api.base-url}") private val baseUrl: String,
    @Value("\${portone.api.imp-key}") private val impKey: String,
    ) {
    fun getPayment(impUid: String, retryCount: Int = 0): PaymentResponse {
        try {
            val response = restClient.get()
                .uri("$baseUrl/payments/$impUid")
                .retrieve()
                .toEntity<PaymentResponse>()
                .body

            return response ?: throw PortOneApiException(
                httpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
                message = "결제 정보 응답이 null입니다",
                errorCode = "PAYMENT_RESPONSE_NULL"
            )
        } catch (e: HttpClientErrorException) {
            if (e.statusCode == HttpStatus.UNAUTHORIZED && retryCount == 0) {
                // 토큰 만료 시 토큰을 무효화하고 한 번만 재시도
                tokenManager.invalidateToken(impKey)
                return getPayment(impUid, retryCount + 1)
            }

            // 재시도 했는데도 인증 오류거나 다른 클라이언트 에러면 그대로 예외 발생
            throw PortOneApiException(
                httpStatus = e.statusCode,
                message = "포트원 결제 정보 조회 실패: ${e.message}",
                errorCode = "PAYMENT_CLIENT_ERROR",
                cause = e
            )
        }
    }

    fun cancelPayment(impUid: String): PaymentResponse {
        val response =
            restClient.post().uri("$baseUrl/payments/cancel").contentType(MediaType.APPLICATION_JSON).body(
                mapOf(
                    "imp_uid" to impUid
                )
            ).retrieve().toEntity<PaymentResponse>().body

        if (response != null) {
            return response
        } else {
            throw PortOneApiException(
                httpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
                message = "결제 취소 응답이 null입니다",
                errorCode = "CANCEL_RESPONSE_NULL"
            )
        }
    }
}
