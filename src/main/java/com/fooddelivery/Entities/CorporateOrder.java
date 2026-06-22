package com.fooddelivery.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// CorporateOrder stores company orders.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "corporate_orders")
public class CorporateOrder extends BaseEntity {

    @Column(unique = true)
    private String corporateCode;

    private String companyName;

    private String costCenter;

    private LocalDateTime orderDate;

    private String status;

    private Double totalAmount = 0.0;

    // Many corporate orders belong to one restaurant.
    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    // One corporate order has many corporate order items.
    @OneToMany(mappedBy = "corporateOrder", cascade = CascadeType.ALL)
    private List<CorporateOrderItem> corporateOrderItems = new ArrayList<>();
}