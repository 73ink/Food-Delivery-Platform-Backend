package com.fooddelivery.DTO.summary;

import com.fooddelivery.Entities.Payment;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentSummaryDTO {

    private Integer id;
    private String paymentMethod;
    private String status;
    private Double amount;

    public static PaymentSummaryDTO fromEntity(Payment payment) {
        if (payment == null) {
            return null;
        }

        return new PaymentSummaryDTO(
                payment.getId(),
                payment.getPaymentMethod(),
                payment.getStatus(),
                payment.getAmount()
        );
    }
}