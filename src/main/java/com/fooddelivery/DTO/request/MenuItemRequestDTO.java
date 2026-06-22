package com.fooddelivery.DTO.request;

import com.fooddelivery.Entities.MenuItem;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

// Request DTO used when adding a menu item.
@Getter
@Setter
public class MenuItemRequestDTO {

    @NotBlank(message = "Menu item name is required")
    private String name;

    private String description;

    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private Double price;

    private Boolean isAvailable = true;

    private Boolean isVegetarian = false;

    @PositiveOrZero(message = "Calories cannot be negative")
    private Integer calories;

    public MenuItem toEntity() {
        MenuItem item = new MenuItem();
        item.setName(name);
        item.setDescription(description);
        item.setPrice(price);
        item.setIsAvailable(isAvailable != null ? isAvailable : true);
        item.setIsVegetarian(isVegetarian != null ? isVegetarian : false);
        item.setCalories(calories);
        return item;
    }

    public void applyTo(MenuItem item) {
        item.setName(name);
        item.setDescription(description);
        item.setPrice(price);
        item.setIsAvailable(isAvailable != null ? isAvailable : true);
        item.setIsVegetarian(isVegetarian != null ? isVegetarian : false);
        item.setCalories(calories);
    }
}