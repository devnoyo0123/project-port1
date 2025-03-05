package com.portone.payment.service

import com.portone.payment.client.PortOneApiClient
import com.portone.payment.client.model.response.PaymentResponse
import com.portone.payment.client.model.response.PaymentStatus
import org.springframework.stereotype.Service

@Service
class PaymentService(
    private val portOneApiClient: PortOneApiClient
) {

    /**
     * 결제 정보 단건조회 및 취소를 수행합니다.
     * 결제가 승인 상태인 경우 취소 요청을 하고 취소 결과를 반환합니다.
     * 결제가 승인 상태가 아닌 경우 조회 결과를 그대로 반환합니다.
     */
    fun getAndCancelPaymentIfPaid(impUid: String): PaymentResponse {
        // 결제 정보 단건조회
        val paymentDetails = portOneApiClient.getPayment(impUid)

        // 응답에서 결제 상태 확인
        val responseData = paymentDetails.response
        val status = responseData?.status?.let { PaymentStatus.fromCode(it) }

        // 결제 상태가 "paid"인 경우에만 취소 요청
        return if (status == PaymentStatus.PAID) {
            // 결제 취소 API 호출
            portOneApiClient.cancelPayment(impUid)
        } else {
            // 결제 승인 상태가 아닌 경우 조회 결과 그대로 반환
            paymentDetails
        }
    }
}
