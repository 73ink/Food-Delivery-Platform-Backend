package com.fooddelivery.Entities;

import jakarta.persistence.*;
import lombok.*;

// CustomerAddress stores delivery addresses for each customer.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customer_addresses")
public class CustomerAddress extends BaseEntity {

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private String city;

    private String building;

    private Boolean isDefault = false;

    // Many addresses belong to one customer.
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
}