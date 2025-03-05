package com.portone.payment.api.exception

import org.springframework.http.HttpStatusCode

/**
 * 포트원 API 관련 예외
 */
class PortOneApiException(
    val httpStatus: HttpStatusCode,
    override val message: String,
    val errorCode: String,
    cause: Throwable? = null
) : RuntimeException(message, cause)
