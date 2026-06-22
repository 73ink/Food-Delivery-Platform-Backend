package com.fooddelivery.DTO.response;

import com.fooddelivery.Entities.Payment;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDTO {

    private Integer id;
    private String paymentMethod;
    private String status;
    private Double amount;
    private String transactionRef;
    private LocalDateTime processedAt;
    private Integer orderId;
    private String orderCode;

    public static PaymentResponseDTO fromEntity(Payment payment) {
        if (payment == null) {
            return null;
        }

        return new PaymentResponseDTO(
                payment.getId(),
                payment.getPaymentMethod(),
                payment.getStatus(),
                payment.getAmount(),
                payment.getTransactionRef(),
                payment.getProcessedAt(),
                payment.getOrder() != null ? payment.getOrder().getId() : null,
                payment.getOrder() != null ? payment.getOrder().getOrderCode() : null
        );
    }
}