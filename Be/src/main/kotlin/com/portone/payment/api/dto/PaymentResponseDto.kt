package com.portone.payment.api.dto

import com.portone.payment.client.model.response.PaymentInfo
import com.portone.payment.client.model.response.PaymentResponse


// API 응답용 DTO - 필드 이름과 구조는 원본과 동일하게 유지
data class PaymentResponseDto(
    val code: Int,
    val message: String? = null,
    val response: PaymentInfoDto? = null
)

data class PaymentInfoDto(
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


fun PaymentResponse.toDto(): PaymentResponseDto {
    return PaymentResponseDto(
        code = this.code,
        message = this.message,
        response = this.response?.toDto()
    )
}

fun PaymentInfo.toDto(): PaymentInfoDto {
    return PaymentInfoDto(
        imp_uid = this.imp_uid,
        merchant_uid = this.merchant_uid,
        status = this.status,
        amount = this.amount,
        name = this.name,
        paid_at = this.paid_at,
        pay_method = this.pay_method,
        pg_provider = this.pg_provider,
        pg_tid = this.pg_tid,
        buyer_name = this.buyer_name,
        buyer_email = this.buyer_email,
        buyer_tel = this.buyer_tel,
        buyer_addr = this.buyer_addr,
        buyer_postcode = this.buyer_postcode,
        custom_data = this.custom_data,
        cancel_amount = this.cancel_amount,
        cancelled_at = this.cancelled_at,
        cancel_reason = this.cancel_reason,
        receipt_url = this.receipt_url,
        fail_reason = this.fail_reason,
        failed_at = this.failed_at,
        card_name = this.card_name,
        card_number = this.card_number,
        card_quota = this.card_quota,
        vbank_name = this.vbank_name,
        vbank_num = this.vbank_num,
        vbank_holder = this.vbank_holder,
        vbank_date = this.vbank_date,
        vbank_issued_at = this.vbank_issued_at
    )
}
