package com.fooddelivery.DTO.request;

import com.fooddelivery.Entities.Restaurant;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

// Request DTO used when creating or updating a restaurant.
@Getter
@Setter
public class RestaurantRequestDTO {

    @NotBlank(message = "Restaurant name is required")
    private String name;

    private String description;

    @NotBlank(message = "Cuisine type is required")
    private String cuisineType;

    @NotBlank(message = "Opening time is required")
    private String openingTime;

    @NotBlank(message = "Closing time is required")
    private String closingTime;

    @DecimalMin(value = "0.0", message = "Minimum order amount cannot be negative")
    private Double minOrderAmount = 0.0;

    @DecimalMin(value = "0.0", message = "Delivery fee cannot be negative")
    private Double deliveryFee = 0.0;

    private Boolean acceptingOrders = true;

    public Restaurant toEntity() {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(name);
        restaurant.setDescription(description);
        restaurant.setCuisineType(cuisineType);
        restaurant.setOpeningTime(openingTime);
        restaurant.setClosingTime(closingTime);
        restaurant.setMinOrderAmount(minOrderAmount);
        restaurant.setDeliveryFee(deliveryFee);
        restaurant.setAcceptingOrders(acceptingOrders != null ? acceptingOrders : true);
        return restaurant;
    }

    public void applyTo(Restaurant restaurant) {
        restaurant.setName(name);
        restaurant.setDescription(description);
        restaurant.setCuisineType(cuisineType);
        restaurant.setOpeningTime(openingTime);
        restaurant.setClosingTime(closingTime);
        restaurant.setMinOrderAmount(minOrderAmount);
        restaurant.setDeliveryFee(deliveryFee);
        restaurant.setAcceptingOrders(acceptingOrders != null ? acceptingOrders : true);
    }
}