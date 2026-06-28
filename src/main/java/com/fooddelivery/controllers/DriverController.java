package com.fooddelivery.controllers;

import com.fooddelivery.DTO.request.DriverRequestDTO;
import com.fooddelivery.DTO.response.DeliveryResponseDTO;
import com.fooddelivery.DTO.response.DriverResponseDTO;
import com.fooddelivery.Services.DeliveryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/drivers")
@RequiredArgsConstructor
public class DriverController {

    private final DeliveryService deliveryService;

    @PostMapping
    public ResponseEntity<DriverResponseDTO> registerDriver(@Valid @RequestBody DriverRequestDTO dto) {
        DriverResponseDTO createdDriver = deliveryService.registerDriver(dto);
        return ResponseEntity
                .created(URI.create("/api/drivers/" + createdDriver.getId()))
                .body(createdDriver);
    }

    @GetMapping
    public ResponseEntity<List<DriverResponseDTO>> getAllDrivers() {
        return ResponseEntity.ok(deliveryService.getAllDrivers());
    }

    @GetMapping("/online")
    public ResponseEntity<List<DriverResponseDTO>> getOnlineDrivers() {
        return ResponseEntity.ok(deliveryService.getOnlineDrivers());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<DriverResponseDTO> toggleStatus(
            @PathVariable Integer id,
            @RequestParam boolean isOnline
    ) {
        return ResponseEntity.ok(deliveryService.toggleDriverOnlineStatus(id, isOnline));
    }

    @PutMapping("/{id}/location")
    public ResponseEntity<DriverResponseDTO> updateLocation(
            @PathVariable Integer id,
            @RequestParam double lat,
            @RequestParam double lng
    ) {
        return ResponseEntity.ok(deliveryService.updateDriverLocation(id, lat, lng));
    }

    @GetMapping("/{id}/deliveries")
    public ResponseEntity<List<DeliveryResponseDTO>> getDriverDeliveries(@PathVariable Integer id) {
        return ResponseEntity.ok(deliveryService.getDriverDeliveryHistory(id));
    }

    @GetMapping("/{id}/deliveries/active")
    public ResponseEntity<DeliveryResponseDTO> getActiveDelivery(@PathVariable Integer id) {
        return ResponseEntity.ok(deliveryService.getActiveDeliveryForDriver(id));
    }
}