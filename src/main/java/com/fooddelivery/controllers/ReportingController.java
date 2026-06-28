package com.fooddelivery.controllers;

import com.fooddelivery.DTO.response.CustomerResponseDTO;
import com.fooddelivery.Services.ReportingService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportingController {

    private final ReportingService reportingService;

    @GetMapping(value = "/revenue/restaurant/{restaurantId}", params = "date")
    public ResponseEntity<Map<String, Object>> restaurantRevenueOnDate(
            @PathVariable Integer restaurantId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ResponseEntity.ok(reportingService.getRestaurantRevenueOnDate(restaurantId, date));
    }

    @GetMapping("/orders/count/restaurant/{restaurantId}")
    public ResponseEntity<Map<String, Object>> completedOrdersCount(@PathVariable Integer restaurantId) {
        return ResponseEntity.ok(reportingService.getCompletedOrderCountForRestaurant(restaurantId));
    }

    @GetMapping("/customers/top-loyalty")
    public ResponseEntity<List<CustomerResponseDTO>> topLoyaltyCustomers() {
        return ResponseEntity.ok(reportingService.getTopLoyaltyCustomers());
    }

    @GetMapping("/drivers/leaderboard")
    public ResponseEntity<List<Map<String, Object>>> driversLeaderboard() {
        return ResponseEntity.ok(reportingService.getDriversLeaderboard());
    }

    @GetMapping("/platform/daily-summary")
    public ResponseEntity<Map<String, Object>> platformDailySummary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ResponseEntity.ok(reportingService.getPlatformDailySummary(date));
    }

    @GetMapping(value = "/revenue/restaurant/{restaurantId}", params = {"from", "to"})
    public ResponseEntity<Map<String, Object>> restaurantRevenueBetweenDates(
            @PathVariable Integer restaurantId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return ResponseEntity.ok(reportingService.getRestaurantRevenueBetweenDates(restaurantId, from, to));
    }

    @GetMapping("/drivers/{driverId}/earnings")
    public ResponseEntity<Map<String, Object>> driverEarnings(
            @PathVariable Integer driverId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return ResponseEntity.ok(reportingService.getDriverEarningsBetweenDates(driverId, from, to));
    }

    @GetMapping("/orders/cancellation-rate")
    public ResponseEntity<Map<String, Object>> cancellationRate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return ResponseEntity.ok(reportingService.getCancellationRate(from, to));
    }

    @GetMapping("/platform/busiest-hours")
    public ResponseEntity<List<Map<String, Object>>> busiestHours() {
        return ResponseEntity.ok(reportingService.getBusiestHours());
    }
}