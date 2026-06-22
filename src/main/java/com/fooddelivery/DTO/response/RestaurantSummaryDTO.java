package com.fooddelivery.DTO.response;

import com.fooddelivery.Entities.Restaurant;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantSummaryDTO {

    private Integer id;
    private String name;
    private String cuisineType;
    private Double deliveryFee;
    private Boolean acceptingOrders;

    public static RestaurantSummaryDTO fromEntity(Restaurant restaurant) {
        if (restaurant == null) {
            return null;
        }

        return new RestaurantSummaryDTO(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getCuisineType(),
                restaurant.getDeliveryFee(),
                restaurant.getAcceptingOrders()
        );
    }
}