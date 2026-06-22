package com.fooddelivery.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

// ComboMeal stores a meal bundle that contains many menu items.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "combo_meals")
public class ComboMeal extends BaseEntity {

    @Column(nullable = false)
    private String comboName;

    private String description;

    private Double totalPrice;

    private Boolean isAvailable = true;

    // Many combo meals belong to one restaurant.
    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    // Many combo meals contain many menu items.
    @ManyToMany
    @JoinTable(
            name = "combo_meal_items",
            joinColumns = @JoinColumn(name = "combo_meal_id"),
            inverseJoinColumns = @JoinColumn(name = "menu_item_id")
    )
    private List<MenuItem> menuItems = new ArrayList<>();
}