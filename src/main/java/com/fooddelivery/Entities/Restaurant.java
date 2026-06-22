package com.fooddelivery.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

// Restaurant entity stores restaurant profile and delivery settings.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "restaurants")
public class Restaurant extends BaseEntity {

    @Column(nullable = false)
    private String name;

    private String description;

    private String cuisineType;

    // Stored as String for simplicity, example: "09:00"
    private String openingTime;

    // Stored as String for simplicity, example: "23:00"
    private String closingTime;

    private Double minOrderAmount = 0.0;

    private Double deliveryFee = 0.0;

    private Boolean acceptingOrders = true;

    // Many restaurants belong to one owner.
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private RestaurantOwner restaurantOwner;

    // One restaurant has many menu items.
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<MenuItem> menuItems = new ArrayList<>();

    // One restaurant has many combo meals.
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<ComboMeal> comboMeals = new ArrayList<>();

    // One restaurant receives many orders.
    @OneToMany(mappedBy = "restaurant")
    private List<Order> orders = new ArrayList<>();

    // One restaurant can have many corporate orders.
    @OneToMany(mappedBy = "restaurant")
    private List<CorporateOrder> corporateOrders = new ArrayList<>();

    // One restaurant can receive many reviews.
    @OneToMany(mappedBy = "restaurant")
    private List<Review> reviews = new ArrayList<>();
}