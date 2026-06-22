package com.fooddelivery.DTO.request;

import com.fooddelivery.Entities.ComboMeal;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

// Request DTO used when creating a combo meal.
@Getter
@Setter
public class ComboMealRequestDTO {

    @NotBlank(message = "Combo name is required")
    private String comboName;

    private String description;

    @DecimalMin(value = "0.0", inclusive = false, message = "Total price must be greater than 0")
    private Double totalPrice;

    private Boolean isAvailable = true;

    @NotEmpty(message = "Combo meal must contain at least one menu item")
    private List<Integer> menuItemIds;

    public ComboMeal toEntity() {
        ComboMeal comboMeal = new ComboMeal();
        comboMeal.setComboName(comboName);
        comboMeal.setDescription(description);
        comboMeal.setTotalPrice(totalPrice);
        comboMeal.setIsAvailable(isAvailable != null ? isAvailable : true);
        return comboMeal;
    }
}