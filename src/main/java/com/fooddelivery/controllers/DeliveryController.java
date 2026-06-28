package com.fooddelivery.controllers;

import com.fooddelivery.DTO.response.DeliveryResponseDTO;
import com.fooddelivery.DTO.response.DriverResponseDTO;
import com.fooddelivery.Services.DeliveryService;
import com.fooddelivery.Services.ReportingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;
    private final ReportingService reportingService;

    @PostMapping("/order/{orderId}/assign-manual/{driverId}")
    public ResponseEntity<DeliveryResponseDTO> assignManual(
            @PathVariable Integer orderId,
            @PathVariable Integer driverId
    ) {
        DeliveryResponseDTO delivery = deliveryService.assignDriverToOrder(orderId, driverId);
        return ResponseEntity
                .created(URI.create("/api/deliveries/" + delivery.getId()))
                .body(delivery);
    }

    @PostMapping("/order/{orderId}/assign-auto")
    public ResponseEntity<DeliveryResponseDTO> assignAuto(@PathVariable Integer orderId) {
        DeliveryResponseDTO delivery = deliveryService.autoAssignDriver(orderId);
        return ResponseEntity
                .created(URI.create("/api/deliveries/" + delivery.getId()))
                .body(delivery);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeliveryResponseDTO> getDeliveryById(@PathVariable Integer id) {
        return ResponseEntity.ok(deliveryService.getDeliveryById(id));
    }

    @PutMapping("/{id}/pickup")
    public ResponseEntity<DeliveryResponseDTO> markPickedUp(@PathVariable Integer id) {
        return ResponseEntity.ok(deliveryService.markDeliveryPickedUp(id));
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<DeliveryResponseDTO> markDelivered(@PathVariable Integer id) {
        return ResponseEntity.ok(deliveryService.markDeliveryDelivered(id));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<DeliveryResponseDTO>> getDeliveriesByStatus(@PathVariable String status) {
        return ResponseEntity.ok(deliveryService.getDeliveriesByStatus(status));
    }

    @GetMapping("/drivers/nearby")
    public ResponseEntity<List<DriverResponseDTO>> nearbyDrivers(
            @RequestParam double lat,
            @RequestParam double lng,
            @RequestParam double radiusKm
    ) {
        return ResponseEntity.ok(deliveryService.getNearbyOnlineDrivers(lat, lng, radiusKm));
    }

    @GetMapping("/drivers/{driverId}/performance")
    public ResponseEntity<Map<String, Object>> driverPerformance(@PathVariable Integer driverId) {
        return ResponseEntity.ok(reportingService.getDriverPerformance(driverId));
    }
}