package com.fooddelivery.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

// RestaurantOwner stores restaurant owner account information.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "restaurant_owners")
public class RestaurantOwner extends BaseEntity {

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    private String phone;

    private String passwordHash;

    @Column(unique = true)
    private String businessLicenseCode;

    // One owner can own many restaurants.
    @OneToMany(mappedBy = "restaurantOwner", cascade = CascadeType.ALL)
    private List<Restaurant> restaurants = new ArrayList<>();
}