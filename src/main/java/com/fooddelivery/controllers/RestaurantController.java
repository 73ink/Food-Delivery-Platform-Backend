package com.fooddelivery.controllers;

import com.fooddelivery.DTO.request.ComboMealRequestDTO;
import com.fooddelivery.DTO.request.MenuItemRequestDTO;
import com.fooddelivery.DTO.request.RestaurantRequestDTO;
import com.fooddelivery.DTO.response.ComboMealResponseDTO;
import com.fooddelivery.DTO.response.MenuItemResponseDTO;
import com.fooddelivery.DTO.response.RestaurantResponseDTO;
import com.fooddelivery.Services.ComboMealService;
import com.fooddelivery.Services.MenuItemService;
import com.fooddelivery.Services.RestaurantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final MenuItemService menuItemService;
    private final ComboMealService comboMealService;

    @PostMapping("/owner/{ownerId}")
    public ResponseEntity<RestaurantResponseDTO> createRestaurant(
            @PathVariable Integer ownerId,
            @Valid @RequestBody RestaurantRequestDTO dto
    ) {
        RestaurantResponseDTO createdRestaurant = restaurantService.createRestaurant(dto, ownerId);
        return ResponseEntity
                .created(URI.create("/api/restaurants/" + createdRestaurant.getId()))
                .body(createdRestaurant);
    }

    @GetMapping
    public ResponseEntity<List<RestaurantResponseDTO>> getAllRestaurants() {
        return ResponseEntity.ok(restaurantService.getAllRestaurants());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponseDTO> getRestaurantById(@PathVariable Integer id) {
        return ResponseEntity.ok(restaurantService.getRestaurantById(id));
    }

    @GetMapping("/cuisine/{cuisine}")
    public ResponseEntity<List<RestaurantResponseDTO>> getByCuisine(@PathVariable String cuisine) {
        return ResponseEntity.ok(restaurantService.getRestaurantsByCuisine(cuisine));
    }

    @GetMapping("/search")
    public ResponseEntity<List<RestaurantResponseDTO>> searchByKeyword(@RequestParam String keyword) {
        return ResponseEntity.ok(restaurantService.searchRestaurantsByKeyword(keyword));
    }

    @GetMapping("/fee")
    public ResponseEntity<List<RestaurantResponseDTO>> getRestaurantsUnderFee(@RequestParam double maxFee) {
        return ResponseEntity.ok(restaurantService.getRestaurantsUnderDeliveryFee(maxFee));
    }

    @PutMapping("/{id}/toggle-orders")
    public ResponseEntity<RestaurantResponseDTO> toggleOrders(
            @PathVariable Integer id,
            @RequestParam boolean accepting
    ) {
        return ResponseEntity.ok(restaurantService.toggleAcceptingOrders(id, accepting));
    }

    @PutMapping("/{id}/fee/{newFee}")
    public ResponseEntity<RestaurantResponseDTO> updateDeliveryFee(
            @PathVariable Integer id,
            @PathVariable double newFee
    ) {
        return ResponseEntity.ok(restaurantService.updateDeliveryFee(id, newFee));
    }

    @GetMapping("/{id}/menu")
    public ResponseEntity<List<MenuItemResponseDTO>> getMenu(@PathVariable Integer id) {
        return ResponseEntity.ok(restaurantService.getMenuForRestaurant(id));
    }

    @PostMapping("/{id}/menu")
    public ResponseEntity<MenuItemResponseDTO> addMenuItem(
            @PathVariable Integer id,
            @Valid @RequestBody MenuItemRequestDTO dto
    ) {
        MenuItemResponseDTO createdItem = menuItemService.addMenuItem(id, dto);
        return ResponseEntity
                .created(URI.create("/api/restaurants/menu/" + createdItem.getId()))
                .body(createdItem);
    }

    @PutMapping("/menu/{itemId}/available")
    public ResponseEntity<MenuItemResponseDTO> updateMenuItemAvailability(
            @PathVariable Integer itemId,
            @RequestParam boolean status
    ) {
        return ResponseEntity.ok(menuItemService.updateAvailability(itemId, status));
    }

    @GetMapping("/menu/search")
    public ResponseEntity<List<MenuItemResponseDTO>> searchMenuItems(
            @RequestParam String keyword,
            @RequestParam(required = false) Integer minCalories,
            @RequestParam(required = false) Integer maxCalories
    ) {
        return ResponseEntity.ok(menuItemService.searchMenuItems(keyword, minCalories, maxCalories));
    }

    @GetMapping("/{id}/combos")
    public ResponseEntity<List<ComboMealResponseDTO>> getCombos(@PathVariable Integer id) {
        return ResponseEntity.ok(comboMealService.getComboMealsByRestaurant(id));
    }

    @PostMapping("/{id}/combos")
    public ResponseEntity<ComboMealResponseDTO> createComboMeal(
            @PathVariable Integer id,
            @Valid @RequestBody ComboMealRequestDTO dto
    ) {
        ComboMealResponseDTO createdCombo = comboMealService.createComboMeal(id, dto);
        return ResponseEntity
                .created(URI.create("/api/restaurants/combos/" + createdCombo.getId()))
                .body(createdCombo);
    }

    @PutMapping("/{id}/bulk-price-increase")
    public ResponseEntity<List<MenuItemResponseDTO>> bulkPriceIncrease(
            @PathVariable Integer id,
            @RequestParam double percentage
    ) {
        return ResponseEntity.ok(restaurantService.bulkUpdateMenuItemPrices(id, percentage));
    }
}