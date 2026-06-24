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


}