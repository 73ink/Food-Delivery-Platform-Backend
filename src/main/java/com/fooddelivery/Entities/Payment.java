package com.fooddelivery.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

// Payment stores payment information for an order.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payments")
public class Payment extends BaseEntity {

    private String paymentMethod;

    private String status;

    private Double amount;

    @Column(unique = true)
    private String transactionRef;

    private LocalDateTime processedAt;

    // One payment belongs to one order.
    @OneToOne
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;
}