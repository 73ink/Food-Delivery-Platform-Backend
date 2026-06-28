package com.fooddelivery.Services;

import com.fooddelivery.DTO.response.ReviewResponseDTO;
import com.fooddelivery.Entities.Customer;
import com.fooddelivery.Entities.DeliveryDriver;
import com.fooddelivery.Entities.Restaurant;
import com.fooddelivery.Entities.Review;
import com.fooddelivery.Exceptions.ResourceNotFoundException;
import com.fooddelivery.Repositories.CustomerRepository;
import com.fooddelivery.Repositories.DeliveryDriverRepository;
import com.fooddelivery.Repositories.RestaurantRepository;
import com.fooddelivery.Repositories.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

// ReviewService handles restaurant and driver reviews.
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final CustomerRepository customerRepository;
    private final RestaurantRepository restaurantRepository;
    private final DeliveryDriverRepository deliveryDriverRepository;

    // Leave review for a restaurant.
    @Transactional
    public ReviewResponseDTO leaveRestaurantReview(Integer customerId, Integer restaurantId, int rating, String comment) {
        Customer customer = findActiveCustomer(customerId);
        Restaurant restaurant = findActiveRestaurant(restaurantId);

        Review review = new Review();
        review.setTargetType("RESTAURANT");
        review.setRating(rating);
        review.setComment(comment);
        review.setCreatedAt(LocalDateTime.now());
        review.setCustomer(customer);
        review.setRestaurant(restaurant);
        review.setDeliveryDriver(null);

        Review savedReview = reviewRepository.save(review);
        return ReviewResponseDTO.fromEntity(savedReview);
    }

    // Leave review for a driver.
    @Transactional
    public ReviewResponseDTO leaveDriverReview(Integer customerId, Integer driverId, int rating, String comment) {
        Customer customer = findActiveCustomer(customerId);
        DeliveryDriver driver = findActiveDriver(driverId);

        Review review = new Review();
        review.setTargetType("DRIVER");
        review.setRating(rating);
        review.setComment(comment);
        review.setCreatedAt(LocalDateTime.now());
        review.setCustomer(customer);
        review.setRestaurant(null);
        review.setDeliveryDriver(driver);

        Review savedReview = reviewRepository.save(review);
        return ReviewResponseDTO.fromEntity(savedReview);
    }

    // Get all reviews for a restaurant.
    @Transactional(readOnly = true)
    public List<ReviewResponseDTO> getReviewsForRestaurant(Integer restaurantId) {
        findActiveRestaurant(restaurantId);

        return reviewRepository.findByRestaurantId(restaurantId)
                .stream()
                .map(ReviewResponseDTO::fromEntity)
                .toList();
    }

    // Get all reviews for a driver.
    @Transactional(readOnly = true)
    public List<ReviewResponseDTO> getReviewsForDriver(Integer driverId) {
        findActiveDriver(driverId);

        return reviewRepository.findByDriverId(driverId)
                .stream()
                .map(ReviewResponseDTO::fromEntity)
                .toList();
    }

    // Get paginated restaurant reviews.
    @Transactional(readOnly = true)
    public Page<ReviewResponseDTO> getRestaurantReviewsPaginated(Integer restaurantId, Pageable pageable) {
        findActiveRestaurant(restaurantId);

        return reviewRepository.findRestaurantReviewsPaginated(restaurantId, pageable)
                .map(ReviewResponseDTO::fromEntity);
    }

    // Average restaurant rating.
    @Transactional(readOnly = true)
    public double getAverageRatingForRestaurant(Integer restaurantId) {
        findActiveRestaurant(restaurantId);
        return reviewRepository.getAverageRatingForRestaurant(restaurantId);
    }

    // Average driver rating.
    @Transactional(readOnly = true)
    public double getAverageRatingForDriver(Integer driverId) {
        findActiveDriver(driverId);
        return reviewRepository.getAverageRatingForDriver(driverId);
    }

    // Soft delete review.
    @Transactional
    public void deleteReview(Integer reviewId) {
        Review review = reviewRepository.findActiveById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with ID: " + reviewId));

        review.setIsActive(false);
        reviewRepository.save(review);
    }

    private Customer findActiveCustomer(Integer customerId) {
        return customerRepository.findActiveById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + customerId));
    }

    private Restaurant findActiveRestaurant(Integer restaurantId) {
        return restaurantRepository.findActiveById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with ID: " + restaurantId));
    }

    private DeliveryDriver findActiveDriver(Integer driverId) {
        return deliveryDriverRepository.findActiveById(driverId)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found with ID: " + driverId));
    }
}