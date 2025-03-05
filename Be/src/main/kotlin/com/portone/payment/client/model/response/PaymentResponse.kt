package com.portone.payment.client.model.response

// 결제응답 파라미터
// 출처: https://developers.portone.io/sdk/ko/v1-sdk/javascript-sdk/payrt?v=v1
data class PaymentResponse(
    val code: Int,
    val message: String? = null,
    val response: PaymentInfo? = null
)

data class PaymentInfo(
    val imp_uid: String,
    val merchant_uid: String,
    val status: String,
    val amount: Int,
    val name: String,
    val paid_at: Long,
    val pay_method: String,
    val pg_provider: String? = null,
    val pg_tid: String? = null,
    val buyer_name: String? = null,
    val buyer_email: String? = null,
    val buyer_tel: String? = null,
    val buyer_addr: String? = null,
    val buyer_postcode: String? = null,
    val custom_data: String? = null,
    val cancel_amount: Int? = null,
    val cancelled_at: Long? = null,
    val cancel_reason: String? = null,
    val receipt_url: String? = null,
    val fail_reason: String? = null,
    val failed_at: Long? = null,
    val card_name: String? = null,
    val card_number: String? = null,
    val card_quota: Int? = null,
    val vbank_name: String? = null,
    val vbank_num: String? = null,
    val vbank_holder: String? = null,
    val vbank_date: Long? = null,
    val vbank_issued_at: Long? = null
)

// 결제상태
// 출처: https://faq.portone.io/ed2439aa-b0bb-421e-8878-a1384e55c261
enum class PaymentStatus(val code: String) {
    PAID("paid"),               // 결제완료
    READY("ready"),             // 미결제 (결제창을 띄우는 순간 ready로 생성됨)
    CANCELLED("cancelled"),     // 결제취소
    FAILED("failed");           // 결제실패

    companion object {
        fun fromCode(code: String): PaymentStatus {
            return PaymentStatus.entries.find { it.code == code }
                ?: throw IllegalArgumentException("Invalid payment status code: $code")
        }
    }
}
