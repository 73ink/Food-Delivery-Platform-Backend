package com.fooddelivery.DTO.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

// Request DTO used when leaving a review.
@Getter
@Setter
public class ReviewRequestDTO {

    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating cannot be more than 5")
    private Integer rating;

    @NotBlank(message = "Comment is required")
    private String comment;
}