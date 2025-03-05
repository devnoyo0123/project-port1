package com.portone.payment.client.model.request

data class PaymentRequest(
    val merchant_uid: String,
    val name: String,
    val amount: Int
)
