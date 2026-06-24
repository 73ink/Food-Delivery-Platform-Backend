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

    // Get menu item by ID.
    @Transactional(readOnly = true)
    public MenuItemResponseDTO getMenuItemById(Integer itemId) {
        MenuItem item = findActiveMenuItem(itemId);

        return MenuItemResponseDTO.fromEntity(item);
    }

    // Get all active menu items.
    @Transactional(readOnly = true)
    public List<MenuItemResponseDTO> getAllMenuItems() {
        return menuItemRepository.findAllActive()
                .stream()
                .map(MenuItemResponseDTO::fromEntity)
                .toList();
    }



    // Private helper method.
    private MenuItem findActiveMenuItem(Integer itemId) {
        return menuItemRepository.findActiveById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with ID: " + itemId));
    }
}