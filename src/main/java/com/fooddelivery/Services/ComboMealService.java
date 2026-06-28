package com.fooddelivery.Services;

import com.fooddelivery.DTO.request.ComboMealRequestDTO;
import com.fooddelivery.DTO.response.ComboMealResponseDTO;
import com.fooddelivery.Entities.ComboMeal;
import com.fooddelivery.Entities.MenuItem;
import com.fooddelivery.Entities.Restaurant;
import com.fooddelivery.Exceptions.ResourceNotFoundException;
import com.fooddelivery.Repositories.ComboMealRepository;
import com.fooddelivery.Repositories.MenuItemRepository;
import com.fooddelivery.Repositories.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

// ComboMealService contains business logic related to combo meals.
@Service
@RequiredArgsConstructor
public class ComboMealService {

    private final ComboMealRepository comboMealRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;

    // Create a new combo meal for a restaurant.
    @Transactional
    public ComboMealResponseDTO createComboMeal(Integer restaurantId, ComboMealRequestDTO dto) {

        Restaurant restaurant = restaurantRepository.findActiveById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with ID: " + restaurantId));

        ComboMeal comboMeal = dto.toEntity();
        comboMeal.setRestaurant(restaurant);

        List<MenuItem> menuItems = new ArrayList<>();

        // Load every menu item by ID and add it to the combo.
        for (Integer menuItemId : dto.getMenuItemIds()) {
            MenuItem item = menuItemRepository.findActiveById(menuItemId)
                    .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with ID: " + menuItemId));

            menuItems.add(item);
        }

        comboMeal.setMenuItems(menuItems);

        ComboMeal savedComboMeal = comboMealRepository.save(comboMeal);

        return ComboMealResponseDTO.fromEntity(savedComboMeal);
    }



    // Private helper method.
    private ComboMeal findActiveComboMeal(Integer comboId) {
        return comboMealRepository.findActiveById(comboId)
                .orElseThrow(() -> new ResourceNotFoundException("Combo meal not found with ID: " + comboId));
    }
}