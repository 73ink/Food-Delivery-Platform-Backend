package com.fooddelivery.Services;

import com.fooddelivery.DTO.request.RestaurantRequestDTO;
import com.fooddelivery.DTO.response.MenuItemResponseDTO;
import com.fooddelivery.DTO.response.RestaurantResponseDTO;
import com.fooddelivery.Entities.MenuItem;
import com.fooddelivery.Entities.Restaurant;
import com.fooddelivery.Entities.RestaurantOwner;
import com.fooddelivery.Exceptions.ResourceNotFoundException;
import com.fooddelivery.Repositories.MenuItemRepository;
import com.fooddelivery.Repositories.RestaurantOwnerRepository;
import com.fooddelivery.Repositories.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// RestaurantService contains business logic related to restaurants.
@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantOwnerRepository restaurantOwnerRepository;
    private final MenuItemRepository menuItemRepository;

    // Create a restaurant and assign it to an existing restaurant owner.
    @Transactional
    public RestaurantResponseDTO createRestaurant(RestaurantRequestDTO dto, Integer ownerId) {

        RestaurantOwner owner = restaurantOwnerRepository.findActiveById(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant owner not found with ID: " + ownerId));

        Restaurant restaurant = dto.toEntity();
        restaurant.setRestaurantOwner(owner);

        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

        return RestaurantResponseDTO.fromEntity(savedRestaurant);
    }

    // Get all active restaurants.
    @Transactional(readOnly = true)
    public List<RestaurantResponseDTO> getAllRestaurants() {
        return restaurantRepository.findAllActive()
                .stream()
                .map(RestaurantResponseDTO::fromEntity)
                .toList();
    }

    // Get restaurant by ID.
    @Transactional(readOnly = true)
    public RestaurantResponseDTO getRestaurantById(Integer restaurantId) {
        Restaurant restaurant = findActiveRestaurant(restaurantId);

        return RestaurantResponseDTO.fromEntity(restaurant);
    }

    // Toggle accepting orders true/false.
    @Transactional
    public RestaurantResponseDTO toggleAcceptingOrders(Integer restaurantId, boolean status) {
        Restaurant restaurant = findActiveRestaurant(restaurantId);

        restaurant.setAcceptingOrders(status);

        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

        return RestaurantResponseDTO.fromEntity(savedRestaurant);
    }

    // Update restaurant delivery fee.
    @Transactional
    public RestaurantResponseDTO updateDeliveryFee(Integer restaurantId, double newFee) {
        Restaurant restaurant = findActiveRestaurant(restaurantId);

        restaurant.setDeliveryFee(newFee);

        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

        return RestaurantResponseDTO.fromEntity(savedRestaurant);
    }

    // Get restaurants by cuisine type.
    @Transactional(readOnly = true)
    public List<RestaurantResponseDTO> getRestaurantsByCuisine(String cuisine) {
        return restaurantRepository.findByCuisineTypeIgnoreCase(cuisine)
                .stream()
                .map(RestaurantResponseDTO::fromEntity)
                .toList();
    }

    // Get restaurants with delivery fee under or equal to a specific amount.
    @Transactional(readOnly = true)
    public List<RestaurantResponseDTO> getRestaurantsUnderDeliveryFee(double maxFee) {
        return restaurantRepository.findByDeliveryFeeLessThanEqual(maxFee)
                .stream()
                .map(RestaurantResponseDTO::fromEntity)
                .toList();
    }

    // Search restaurant by name keyword.
    @Transactional(readOnly = true)
    public List<RestaurantResponseDTO> searchRestaurantsByKeyword(String keyword) {
        return restaurantRepository.searchByNameKeyword(keyword)
                .stream()
                .map(RestaurantResponseDTO::fromEntity)
                .toList();
    }

    // Get menu items for a restaurant.
    @Transactional(readOnly = true)
    public List<MenuItemResponseDTO> getMenuForRestaurant(Integer restaurantId) {
        findActiveRestaurant(restaurantId);

        return menuItemRepository.findByRestaurantId(restaurantId)
                .stream()
                .map(MenuItemResponseDTO::fromEntity)
                .toList();
    }



    // Private helper method to avoid repeating restaurant lookup.
    private Restaurant findActiveRestaurant(Integer restaurantId) {
        return restaurantRepository.findActiveById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with ID: " + restaurantId));
    }
}