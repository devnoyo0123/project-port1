package com.portone.payment.client.model.response

// 인증 관련
// 출처: https://developers.portone.io/api/rest-v1/auth?v=v1
data class TokenResponse(
    val code: Int,
    val message: String? = null,
    val response: TokenDetail
)

data class TokenDetail(
    val access_token: String,
    val expired_at: Long,
    val now: Long
)
