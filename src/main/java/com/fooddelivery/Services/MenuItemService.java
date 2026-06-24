package com.fooddelivery.Services;

import com.fooddelivery.DTO.request.MenuItemRequestDTO;
import com.fooddelivery.DTO.response.MenuItemResponseDTO;
import com.fooddelivery.Entities.MenuItem;
import com.fooddelivery.Entities.Restaurant;
import com.fooddelivery.Exceptions.ResourceNotFoundException;
import com.fooddelivery.Repositories.MenuItemRepository;
import com.fooddelivery.Repositories.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// MenuItemService contains business logic related to menu items.
@Service
@RequiredArgsConstructor
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;

    // Add a new menu item to a restaurant.
    @Transactional
    public MenuItemResponseDTO addMenuItem(Integer restaurantId, MenuItemRequestDTO dto) {

        Restaurant restaurant = restaurantRepository.findActiveById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with ID: " + restaurantId));

        MenuItem item = dto.toEntity();
        item.setRestaurant(restaurant);

        MenuItem savedItem = menuItemRepository.save(item);

        return MenuItemResponseDTO.fromEntity(savedItem);
    }


}