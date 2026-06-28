package com.fooddelivery.controllers;

import com.fooddelivery.DTO.request.RestaurantOwnerRequestDTO;
import com.fooddelivery.DTO.response.RestaurantOwnerResponseDTO;
import com.fooddelivery.Services.RestaurantOwnerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/owners")
@RequiredArgsConstructor
public class RestaurantOwnerController {

    private final RestaurantOwnerService restaurantOwnerService;

    @PostMapping
    public ResponseEntity<RestaurantOwnerResponseDTO> createOwner(
            @Valid @RequestBody RestaurantOwnerRequestDTO dto
    ) {
        RestaurantOwnerResponseDTO createdOwner = restaurantOwnerService.createRestaurantOwner(dto);
        return ResponseEntity
                .created(URI.create("/api/owners/" + createdOwner.getId()))
                .body(createdOwner);
    }

    @GetMapping
    public ResponseEntity<List<RestaurantOwnerResponseDTO>> getAllOwners() {
        return ResponseEntity.ok(restaurantOwnerService.getAllOwners());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantOwnerResponseDTO> getOwnerById(@PathVariable Integer id) {
        return ResponseEntity.ok(restaurantOwnerService.getOwnerById(id));
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateOwner(@PathVariable Integer id) {
        restaurantOwnerService.deactivateOwner(id);
        return ResponseEntity.noContent().build();
    }
}