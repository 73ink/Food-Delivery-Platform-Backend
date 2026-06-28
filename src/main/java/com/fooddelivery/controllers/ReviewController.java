package com.fooddelivery.controllers;

import com.fooddelivery.DTO.request.ReviewRequestDTO;
import com.fooddelivery.DTO.response.ReviewResponseDTO;
import com.fooddelivery.Services.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/restaurant/{restaurantId}/customer/{customerId}")
    public ResponseEntity<ReviewResponseDTO> restaurantReview(
            @PathVariable Integer restaurantId,
            @PathVariable Integer customerId,
            @Valid @RequestBody ReviewRequestDTO dto
    ) {
        ReviewResponseDTO review = reviewService.leaveRestaurantReview(
                customerId,
                restaurantId,
                dto.getRating(),
                dto.getComment()
        );

        return ResponseEntity
                .created(URI.create("/api/reviews/" + review.getId()))
                .body(review);
    }

    @PostMapping("/driver/{driverId}/customer/{customerId}")
    public ResponseEntity<ReviewResponseDTO> driverReview(
            @PathVariable Integer driverId,
            @PathVariable Integer customerId,
            @Valid @RequestBody ReviewRequestDTO dto
    ) {
        ReviewResponseDTO review = reviewService.leaveDriverReview(
                customerId,
                driverId,
                dto.getRating(),
                dto.getComment()
        );

        return ResponseEntity
                .created(URI.create("/api/reviews/" + review.getId()))
                .body(review);
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<?> getRestaurantReviews(
            @PathVariable Integer restaurantId,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        if (page != null && size != null) {
            return ResponseEntity.ok(
                    reviewService.getRestaurantReviewsPaginated(restaurantId, PageRequest.of(page, size))
            );
        }

        return ResponseEntity.ok(reviewService.getReviewsForRestaurant(restaurantId));
    }

    @GetMapping("/driver/{driverId}")
    public ResponseEntity<?> getDriverReviews(@PathVariable Integer driverId) {
        return ResponseEntity.ok(reviewService.getReviewsForDriver(driverId));
    }

    @GetMapping("/restaurant/{restaurantId}/average")
    public ResponseEntity<Double> getRestaurantAverage(@PathVariable Integer restaurantId) {
        return ResponseEntity.ok(reviewService.getAverageRatingForRestaurant(restaurantId));
    }

    @GetMapping("/driver/{driverId}/average")
    public ResponseEntity<Double> getDriverAverage(@PathVariable Integer driverId) {
        return ResponseEntity.ok(reviewService.getAverageRatingForDriver(driverId));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Integer reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }
}