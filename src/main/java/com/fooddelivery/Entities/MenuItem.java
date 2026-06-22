package com.fooddelivery.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

// MenuItem stores individual food items inside a restaurant menu.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "menu_items")
public class MenuItem extends BaseEntity {

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private Double price;

    private Boolean isAvailable = true;

    private Boolean isVegetarian = false;

    private Integer calories;

    // Many menu items belong to one restaurant.
    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    // One menu item can appear in many order items.
    @OneToMany(mappedBy = "menuItem")
    private List<OrderItem> orderItems = new ArrayList<>();

    // One menu item can appear in many corporate order items.
    @OneToMany(mappedBy = "menuItem")
    private List<CorporateOrderItem> corporateOrderItems = new ArrayList<>();

    // Many menu items can be part of many combo meals.
    @ManyToMany(mappedBy = "menuItems")
    private List<ComboMeal> comboMeals = new ArrayList<>();
}