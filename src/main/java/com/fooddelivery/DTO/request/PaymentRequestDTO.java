package com.fooddelivery.DTO.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

// Request DTO used when processing a payment.
@Getter
@Setter
public class PaymentRequestDTO {

    @NotBlank(message = "Payment method is required")
    @Pattern(regexp = "CASH|CARD|ONLINE", message = "Payment method must be CASH, CARD, or ONLINE")
    private String paymentMethod;
}