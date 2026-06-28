package com.fooddelivery.Services;

import com.fooddelivery.DTO.response.PaymentResponseDTO;
import com.fooddelivery.Entities.Order;
import com.fooddelivery.Entities.Payment;
import com.fooddelivery.Exceptions.DuplicateResourceException;
import com.fooddelivery.Exceptions.InvalidOrderStateException;
import com.fooddelivery.Exceptions.ResourceNotFoundException;
import com.fooddelivery.Repositories.OrderRepository;
import com.fooddelivery.Repositories.PaymentRepository;
import com.fooddelivery.Utils.HelperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// PaymentService handles payment creation, completion, refund, and analytics.
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    // Create payment record for an order.
    @Transactional
    public PaymentResponseDTO processPayment(Integer orderId, String method) {
        Order order = findActiveOrder(orderId);

        if (paymentRepository.findByOrderId(orderId).isPresent()) {
            throw new DuplicateResourceException("Payment already exists for this order");
        }

        String formattedMethod = method.toUpperCase();

        if (!isValidPaymentMethod(formattedMethod)) {
            throw new InvalidOrderStateException("Invalid payment method: " + method);
        }

        Payment payment = new Payment();
        payment.setPaymentMethod(formattedMethod);
        payment.setStatus("PENDING");
        payment.setAmount(order.getTotalAmount());
        payment.setTransactionRef(HelperUtils.generateCode("PAY", 8));
        payment.setOrder(order);

        Payment savedPayment = paymentRepository.save(payment);
        return PaymentResponseDTO.fromEntity(savedPayment);
    }

    // Mark payment as completed.
    @Transactional
    public PaymentResponseDTO completePayment(Integer paymentId) {
        Payment payment = findActivePayment(paymentId);

        if ("REFUNDED".equalsIgnoreCase(payment.getStatus())) {
            throw new InvalidOrderStateException("Refunded payment cannot be completed");
        }

        payment.setStatus("COMPLETED");
        payment.setProcessedAt(LocalDateTime.now());

        Payment savedPayment = paymentRepository.save(payment);
        return PaymentResponseDTO.fromEntity(savedPayment);
    }

    // Refund payment by order ID, as required in the service brief.
    @Transactional
    public PaymentResponseDTO refundPayment(Integer orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found for order ID: " + orderId));

        payment.setStatus("REFUNDED");
        payment.setProcessedAt(LocalDateTime.now());

        Payment savedPayment = paymentRepository.save(payment);
        return PaymentResponseDTO.fromEntity(savedPayment);
    }

    // Refund payment by payment ID, useful for controller endpoint PUT /{paymentId}/refund.
    @Transactional
    public PaymentResponseDTO refundPaymentByPaymentId(Integer paymentId) {
        Payment payment = findActivePayment(paymentId);

        payment.setStatus("REFUNDED");
        payment.setProcessedAt(LocalDateTime.now());

        Payment savedPayment = paymentRepository.save(payment);
        return PaymentResponseDTO.fromEntity(savedPayment);
    }

    // Get payment by order ID.
    @Transactional(readOnly = true)
    public PaymentResponseDTO getPaymentByOrderId(Integer orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found for order ID: " + orderId));

        return PaymentResponseDTO.fromEntity(payment);
    }

    // Get payment by payment ID.
    @Transactional(readOnly = true)
    public PaymentResponseDTO getPaymentById(Integer paymentId) {
        Payment payment = findActivePayment(paymentId);
        return PaymentResponseDTO.fromEntity(payment);
    }

    // Filter and paginate payments.
    @Transactional(readOnly = true)
    public Page<PaymentResponseDTO> getPaymentsWithFilters(
            String method,
            String status,
            LocalDateTime fromDate,
            LocalDateTime toDate,
            Pageable pageable
    ) {
        return paymentRepository.findPaymentsWithFilters(method, status, fromDate, toDate, pageable)
                .map(PaymentResponseDTO::fromEntity);
    }

    // Total amount processed, grouped by payment method.
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getPaymentAnalyticsByMethod() {
        return paymentRepository.getPaymentAnalyticsByMethod()
                .stream()
                .map(row -> {
                    Map<String, Object> data = new HashMap<>();
                    data.put("paymentMethod", row[0]);
                    data.put("totalAmount", row[1]);
                    return data;
                })
                .toList();
    }

    private Payment findActivePayment(Integer paymentId) {
        return paymentRepository.findActiveById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with ID: " + paymentId));
    }

    private Order findActiveOrder(Integer orderId) {
        return orderRepository.findActiveById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));
    }

    private boolean isValidPaymentMethod(String method) {
        return method.equals("CASH")
                || method.equals("CARD")
                || method.equals("ONLINE");
    }
}