package com.fooddelivery.Controllers;

import com.fooddelivery.DTO.response.PaymentResponseDTO;
import com.fooddelivery.Services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/order/{orderId}")
    public ResponseEntity<PaymentResponseDTO> createPayment(
            @PathVariable Integer orderId,
            @RequestParam String method
    ) {
        PaymentResponseDTO payment = paymentService.processPayment(orderId, method);
        return ResponseEntity
                .created(URI.create("/api/payments/" + payment.getId()))
                .body(payment);
    }

    @PutMapping("/{paymentId}/complete")
    public ResponseEntity<PaymentResponseDTO> completePayment(@PathVariable Integer paymentId) {
        return ResponseEntity.ok(paymentService.completePayment(paymentId));
    }

    @PutMapping("/{paymentId}/refund")
    public ResponseEntity<PaymentResponseDTO> refundPayment(@PathVariable Integer paymentId) {
        return ResponseEntity.ok(paymentService.refundPaymentByPaymentId(paymentId));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<PaymentResponseDTO> getPaymentByOrder(@PathVariable Integer orderId) {
        return ResponseEntity.ok(paymentService.getPaymentByOrderId(orderId));
    }

    @GetMapping
    public ResponseEntity<Page<PaymentResponseDTO>> getPayments(
            @RequestParam(required = false) String method,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                paymentService.getPaymentsWithFilters(method, status, from, to, PageRequest.of(page, size))
        );
    }

    @GetMapping("/analytics/by-method")
    public ResponseEntity<List<Map<String, Object>>> analyticsByMethod() {
        return ResponseEntity.ok(paymentService.getPaymentAnalyticsByMethod());
    }
}