package com.fooddelivery.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

// Review stores customer reviews for restaurants or drivers.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reviews")
public class Review extends BaseEntity {

    // Example: RESTAURANT or DRIVER
    private String targetType;

    private Integer rating;

    private String comment;

    private LocalDateTime createdAt;

    // Many reviews belong to one customer.
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    // Nullable because a review can target a driver instead.
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    // Nullable because a review can target a restaurant instead.
    @ManyToOne
    @JoinColumn(name = "driver_id")
    private DeliveryDriver deliveryDriver;
}