package com.fooddelivery.DTO.response;

import com.fooddelivery.Entities.MenuItem;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemResponseDTO {

    private Integer id;
    private String name;
    private String description;
    private Double price;
    private Boolean isAvailable;
    private Boolean isVegetarian;
    private Integer calories;
    private RestaurantSummaryDTO restaurant;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public static MenuItemResponseDTO fromEntity(MenuItem item) {
        if (item == null) {
            return null;
        }

        return new MenuItemResponseDTO(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getPrice(),
                item.getIsAvailable(),
                item.getIsVegetarian(),
                item.getCalories(),
                RestaurantSummaryDTO.fromEntity(item.getRestaurant()),
                item.getCreatedDate(),
                item.getUpdatedDate()
        );
    }
}