package com.fooddelivery.Services;

import com.fooddelivery.DTO.response.CustomerResponseDTO;
import com.fooddelivery.Entities.Delivery;
import com.fooddelivery.Entities.Order;
import com.fooddelivery.Repositories.CustomerRepository;
import com.fooddelivery.Repositories.DeliveryRepository;
import com.fooddelivery.Repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

// ReportingService handles analytics and reporting endpoints.
@Service
@RequiredArgsConstructor
public class ReportingService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final DeliveryRepository deliveryRepository;

    // Revenue for a restaurant on a specific day.
    @Transactional(readOnly = true)
    public Map<String, Object> getRestaurantRevenueOnDate(Integer restaurantId, LocalDate date) {
        double revenue = orderRepository.sumDeliveredRevenueForRestaurantOnDate(restaurantId, date);

        Map<String, Object> report = new HashMap<>();
        report.put("restaurantId", restaurantId);
        report.put("date", date);
        report.put("revenue", revenue);

        return report;
    }

    // Count completed orders for one restaurant.
    @Transactional(readOnly = true)
    public Map<String, Object> getCompletedOrderCountForRestaurant(Integer restaurantId) {
        long count = orderRepository.countCompletedOrdersForRestaurant(restaurantId);

        Map<String, Object> report = new HashMap<>();
        report.put("restaurantId", restaurantId);
        report.put("completedOrders", count);

        return report;
    }

    // Top 10 customers by loyalty points.
    @Transactional(readOnly = true)
    public List<CustomerResponseDTO> getTopLoyaltyCustomers() {
        return customerRepository.findTop10ByLoyaltyPoints()
                .stream()
                .map(CustomerResponseDTO::fromEntity)
                .toList();
    }

    // Top drivers by completed deliveries.
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getDriversLeaderboard() {
        Map<Integer, Map<String, Object>> leaderboard = new HashMap<>();

        List<Delivery> deliveredList = deliveryRepository.findAllActive()
                .stream()
                .filter(delivery -> "DELIVERED".equalsIgnoreCase(delivery.getStatus()))
                .filter(delivery -> delivery.getDeliveryDriver() != null)
                .toList();

        for (Delivery delivery : deliveredList) {
            Integer driverId = delivery.getDeliveryDriver().getId();

            leaderboard.putIfAbsent(driverId, new HashMap<>());
            Map<String, Object> row = leaderboard.get(driverId);

            row.put("driverId", driverId);
            row.put("driverName", delivery.getDeliveryDriver().getFirstName() + " " + delivery.getDeliveryDriver().getLastName());

            int completedCount = row.get("completedDeliveries") == null
                    ? 0
                    : (int) row.get("completedDeliveries");

            row.put("completedDeliveries", completedCount + 1);
        }

        return leaderboard.values()
                .stream()
                .sorted((a, b) -> Integer.compare(
                        (int) b.get("completedDeliveries"),
                        (int) a.get("completedDeliveries")
                ))
                .toList();
    }

    // Platform daily summary.
    @Transactional(readOnly = true)
    public Map<String, Object> getPlatformDailySummary(LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay().minusSeconds(1);

        List<Order> orders = orderRepository.findByOrderDateBetween(start, end);

        double totalRevenue = 0.0;
        double deliveryFees = 0.0;
        int totalOrders = orders.size();

        for (Order order : orders) {
            if ("DELIVERED".equalsIgnoreCase(order.getStatus())) {
                totalRevenue += order.getTotalAmount() != null ? order.getTotalAmount() : 0.0;
                deliveryFees += order.getDeliveryFee() != null ? order.getDeliveryFee() : 0.0;
            }
        }

        Map<String, Object> report = new HashMap<>();
        report.put("date", date);
        report.put("totalOrders", totalOrders);
        report.put("deliveredRevenue", totalRevenue);
        report.put("deliveryFeesCollected", deliveryFees);

        return report;
    }

    // Date-range revenue breakdown for a restaurant.
    @Transactional(readOnly = true)
    public Map<String, Object> getRestaurantRevenueBetweenDates(Integer restaurantId, LocalDate from, LocalDate to) {
        LocalDateTime start = from.atStartOfDay();
        LocalDateTime end = to.plusDays(1).atStartOfDay().minusSeconds(1);

        List<Order> orders = orderRepository.findByOrderDateBetween(start, end);

        double revenue = 0.0;
        int deliveredOrders = 0;

        for (Order order : orders) {
            boolean sameRestaurant = order.getRestaurant() != null && order.getRestaurant().getId().equals(restaurantId);
            boolean delivered = "DELIVERED".equalsIgnoreCase(order.getStatus());

            if (sameRestaurant && delivered) {
                revenue += order.getTotalAmount() != null ? order.getTotalAmount() : 0.0;
                deliveredOrders++;
            }
        }

        Map<String, Object> report = new HashMap<>();
        report.put("restaurantId", restaurantId);
        report.put("from", from);
        report.put("to", to);
        report.put("deliveredOrders", deliveredOrders);
        report.put("revenue", revenue);

        return report;
    }

    // Driver earnings report based on delivery fee of delivered orders.
    @Transactional(readOnly = true)
    public Map<String, Object> getDriverEarningsBetweenDates(Integer driverId, LocalDate from, LocalDate to) {
        LocalDateTime start = from.atStartOfDay();
        LocalDateTime end = to.plusDays(1).atStartOfDay().minusSeconds(1);

        List<Delivery> deliveries = deliveryRepository.findAllActive();

        int completedDeliveries = 0;
        double estimatedEarnings = 0.0;

        for (Delivery delivery : deliveries) {
            if (delivery.getDeliveryDriver() == null || !delivery.getDeliveryDriver().getId().equals(driverId)) {
                continue;
            }

            if (!"DELIVERED".equalsIgnoreCase(delivery.getStatus())) {
                continue;
            }

            if (delivery.getDeliveredAt() == null) {
                continue;
            }

            boolean inRange = !delivery.getDeliveredAt().isBefore(start) && !delivery.getDeliveredAt().isAfter(end);

            if (inRange) {
                completedDeliveries++;

                if (delivery.getOrder() != null && delivery.getOrder().getDeliveryFee() != null) {
                    estimatedEarnings += delivery.getOrder().getDeliveryFee();
                }
            }
        }

        Map<String, Object> report = new HashMap<>();
        report.put("driverId", driverId);
        report.put("from", from);
        report.put("to", to);
        report.put("completedDeliveries", completedDeliveries);
        report.put("estimatedEarnings", estimatedEarnings);

        return report;
    }

    // Cancelled vs completed ratio over a period.
    @Transactional(readOnly = true)
    public Map<String, Object> getCancellationRate(LocalDate from, LocalDate to) {
        LocalDateTime start = from.atStartOfDay();
        LocalDateTime end = to.plusDays(1).atStartOfDay().minusSeconds(1);

        List<Order> orders = orderRepository.findByOrderDateBetween(start, end);

        long cancelled = orders.stream()
                .filter(order -> "CANCELLED".equalsIgnoreCase(order.getStatus()))
                .count();

        long completed = orders.stream()
                .filter(order -> "DELIVERED".equalsIgnoreCase(order.getStatus()))
                .count();

        long total = cancelled + completed;

        double cancellationRate = total == 0 ? 0.0 : ((double) cancelled / total) * 100;

        Map<String, Object> report = new HashMap<>();
        report.put("from", from);
        report.put("to", to);
        report.put("cancelledOrders", cancelled);
        report.put("completedOrders", completed);
        report.put("cancellationRatePercentage", cancellationRate);

        return report;
    }

    // Order volume grouped by hour of day.
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getBusiestHours() {
        List<Order> orders = orderRepository.findAllActive();

        Map<Integer, Integer> hourCounts = new HashMap<>();

        for (Order order : orders) {
            if (order.getOrderDate() == null) {
                continue;
            }

            int hour = order.getOrderDate().getHour();
            hourCounts.put(hour, hourCounts.getOrDefault(hour, 0) + 1);
        }

        List<Map<String, Object>> result = new ArrayList<>();

        for (Map.Entry<Integer, Integer> entry : hourCounts.entrySet()) {
            Map<String, Object> row = new HashMap<>();
            row.put("hour", entry.getKey());
            row.put("orderCount", entry.getValue());
            result.add(row);
        }

        result.sort((a, b) -> Integer.compare((int) b.get("orderCount"), (int) a.get("orderCount")));

        return result;
    }

    // Driver performance: completed count, average delivery time, and simple rating placeholder.
    @Transactional(readOnly = true)
    public Map<String, Object> getDriverPerformance(Integer driverId) {
        List<Delivery> deliveries = deliveryRepository.findAllActive();

        int completedCount = 0;
        long totalMinutes = 0;

        for (Delivery delivery : deliveries) {
            if (delivery.getDeliveryDriver() == null || !delivery.getDeliveryDriver().getId().equals(driverId)) {
                continue;
            }

            if (!"DELIVERED".equalsIgnoreCase(delivery.getStatus())) {
                continue;
            }

            completedCount++;

            if (delivery.getPickedUpAt() != null && delivery.getDeliveredAt() != null) {
                totalMinutes += Duration.between(delivery.getPickedUpAt(), delivery.getDeliveredAt()).toMinutes();
            }
        }

        double averageDeliveryTime = completedCount == 0 ? 0.0 : (double) totalMinutes / completedCount;

        Map<String, Object> report = new HashMap<>();
        report.put("driverId", driverId);
        report.put("completedDeliveries", completedCount);
        report.put("averageDeliveryTimeMinutes", averageDeliveryTime);

        return report;
    }
}