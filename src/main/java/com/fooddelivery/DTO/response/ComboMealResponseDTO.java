package com.fooddelivery.DTO.response;

import com.fooddelivery.DTO.summary.RestaurantSummaryDTO;
import com.fooddelivery.Entities.ComboMeal;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ComboMealResponseDTO {

    private Integer id;
    private String comboName;
    private String description;
    private Double totalPrice;
    private Boolean isAvailable;
    private RestaurantSummaryDTO restaurant;
    private List<MenuItemResponseDTO> menuItems;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public static ComboMealResponseDTO fromEntity(ComboMeal comboMeal) {
        if (comboMeal == null) {
            return null;
        }

        List<MenuItemResponseDTO> itemDTOs = null;

        if (comboMeal.getMenuItems() != null) {
            itemDTOs = comboMeal.getMenuItems()
                    .stream()
                    .filter(item -> Boolean.TRUE.equals(item.getIsActive()))
                    .map(MenuItemResponseDTO::fromEntity)
                    .toList();
        }

        return new ComboMealResponseDTO(
                comboMeal.getId(),
                comboMeal.getComboName(),
                comboMeal.getDescription(),
                comboMeal.getTotalPrice(),
                comboMeal.getIsAvailable(),
                RestaurantSummaryDTO.fromEntity(comboMeal.getRestaurant()),
                itemDTOs,
                comboMeal.getCreatedDate(),
                comboMeal.getUpdatedDate()
        );
    }
}