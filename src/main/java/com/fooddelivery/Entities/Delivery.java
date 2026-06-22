package com.fooddelivery.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

// Delivery stores tracking and driver assignment details.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "deliveries")
public class Delivery extends BaseEntity {

    @Column(unique = true)
    private String trackingCode;

    private String status;

    private LocalDateTime assignedAt;

    private LocalDateTime pickedUpAt;

    private LocalDateTime deliveredAt;

    // One delivery belongs to one order.
    @OneToOne
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;

    // Many deliveries can be assigned to one driver.
    @ManyToOne
    @JoinColumn(name = "driver_id", nullable = false)
    private DeliveryDriver deliveryDriver;
}