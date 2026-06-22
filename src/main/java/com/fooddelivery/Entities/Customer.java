package com.fooddelivery.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

// Customer entity stores customer account information.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customers")
public class Customer extends BaseEntity {

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    private String phone;

    // We store passwordHash, not plain password.
    private String passwordHash;

    private Integer loyaltyPoints = 0;

    @Column(unique = true)
    private String customerCode;

    // One customer can have many addresses.
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<CustomerAddress> addresses = new ArrayList<>();

    // One customer can have many orders.
    @OneToMany(mappedBy = "customer")
    private List<Order> orders = new ArrayList<>();

    // One customer can write many reviews.
    @OneToMany(mappedBy = "customer")
    private List<Review> reviews = new ArrayList<>();
}