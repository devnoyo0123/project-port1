package com.portone.payment.api

import com.portone.payment.api.dto.PaymentResponseDto
import com.portone.payment.api.dto.toDto
import com.portone.payment.service.PaymentService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/payments")
class PaymentController(
    private val paymentService: PaymentService,
) {

    /**
     * 결제 정보 단건조회 API
     */
    @GetMapping("/{impUid}")
    fun getPayment(@PathVariable impUid: String): ResponseEntity<PaymentResponseDto> {
        val response = paymentService.getAndCancelPaymentIfPaid(impUid)
        return ResponseEntity.ok(response.toDto())
    }
}
