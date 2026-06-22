package com.fooddelivery.DTO.response;

import com.fooddelivery.Entities.Restaurant;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantResponseDTO {

    private Integer id;
    private String name;
    private String description;
    private String cuisineType;
    private String openingTime;
    private String closingTime;
    private Double minOrderAmount;
    private Double deliveryFee;
    private Boolean acceptingOrders;
    private RestaurantOwnerResponseDTO owner;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public static RestaurantResponseDTO fromEntity(Restaurant restaurant) {
        if (restaurant == null) {
            return null;
        }

        return new RestaurantResponseDTO(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getDescription(),
                restaurant.getCuisineType(),
                restaurant.getOpeningTime(),
                restaurant.getClosingTime(),
                restaurant.getMinOrderAmount(),
                restaurant.getDeliveryFee(),
                restaurant.getAcceptingOrders(),
                RestaurantOwnerResponseDTO.fromEntity(restaurant.getRestaurantOwner()),
                restaurant.getCreatedDate(),
                restaurant.getUpdatedDate()
        );
    }
}