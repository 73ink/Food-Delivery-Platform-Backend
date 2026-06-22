package com.fooddelivery.DTO.response;

import com.fooddelivery.DTO.summary.CustomerSummaryDTO;
import com.fooddelivery.DTO.summary.DriverSummaryDTO;
import com.fooddelivery.DTO.summary.RestaurantSummaryDTO;
import com.fooddelivery.Entities.Review;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponseDTO {

    private Integer id;
    private String targetType;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
    private CustomerSummaryDTO customer;
    private RestaurantSummaryDTO restaurant;
    private DriverSummaryDTO driver;

    public static ReviewResponseDTO fromEntity(Review review) {
        if (review == null) {
            return null;
        }

        return new ReviewResponseDTO(
                review.getId(),
                review.getTargetType(),
                review.getRating(),
                review.getComment(),
                review.getCreatedAt(),
                CustomerSummaryDTO.fromEntity(review.getCustomer()),
                RestaurantSummaryDTO.fromEntity(review.getRestaurant()),
                DriverSummaryDTO.fromEntity(review.getDeliveryDriver())
        );
    }
}