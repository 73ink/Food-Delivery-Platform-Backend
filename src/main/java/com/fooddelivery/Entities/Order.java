package com.fooddelivery.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// Order stores customer food orders.
// Entity name is CustomerOrder to avoid conflict with SQL/JPQL ORDER keyword.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "CustomerOrder")
@Table(name = "orders")
public class Order extends BaseEntity {

    @Column(unique = true)
    private String orderCode;

    private LocalDateTime orderDate;

    private String status;

    private Double subtotal = 0.0;

    private Double deliveryFee = 0.0;

    private Double discountAmount = 0.0;

    private Double totalAmount = 0.0;

    private String deliveryNotes;

    // Many orders belong to one customer.
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    // Many orders belong to one restaurant.
    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    // One order has many order items.
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    // One order has one delivery.
    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Delivery delivery;

    // One order has one payment.
    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Payment payment;
}