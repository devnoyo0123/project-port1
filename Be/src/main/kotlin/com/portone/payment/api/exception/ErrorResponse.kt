package com.portone.payment.api.exception

import java.time.LocalDateTime

/**
 * API 에러 응답 객체
 */
data class ErrorResponse(
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val status: Int,
    val errorCode: String, // 커스텀 에러 코드 (예: "INVALID_AMOUNT", "TOKEN_EXPIRED" 등으로 사용가능)
    val message: String,
    val path: String
)
