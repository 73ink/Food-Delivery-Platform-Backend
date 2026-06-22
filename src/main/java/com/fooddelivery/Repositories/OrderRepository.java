package com.fooddelivery.Repositories;

import com.fooddelivery.Entities.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query("SELECT o FROM CustomerOrder o WHERE o.id = :id AND o.isActive = true")
    Optional<Order> findActiveById(@Param("id") Integer id);

    @Query("SELECT o FROM CustomerOrder o WHERE o.isActive = true")
    List<Order> findAllActive();

    @Query("SELECT o FROM CustomerOrder o WHERE o.customer.id = :customerId AND o.isActive = true")
    List<Order> findByCustomerId(@Param("customerId") Integer customerId);

    @Query("SELECT o FROM CustomerOrder o WHERE o.restaurant.id = :restaurantId AND o.status = :status AND o.isActive = true")
    List<Order> findByRestaurantIdAndStatus(
            @Param("restaurantId") Integer restaurantId,
            @Param("status") String status
    );

    @Query("SELECT o FROM CustomerOrder o WHERE o.orderDate BETWEEN :startDate AND :endDate AND o.isActive = true")
    List<Order> findByOrderDateBetween(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("SELECT COUNT(o) FROM CustomerOrder o WHERE o.restaurant.id = :restaurantId AND o.status = 'DELIVERED' AND o.isActive = true")
    long countCompletedOrdersForRestaurant(@Param("restaurantId") Integer restaurantId);

    @Query(value = "SELECT COALESCE(SUM(total_amount), 0) FROM orders " +
            "WHERE restaurant_id = :restaurantId " +
            "AND status = 'DELIVERED' " +
            "AND DATE(order_date) = :date " +
            "AND is_active = true",
            nativeQuery = true)
    double sumDeliveredRevenueForRestaurantOnDate(
            @Param("restaurantId") Integer restaurantId,
            @Param("date") LocalDate date
    );

    @Query("SELECT o FROM CustomerOrder o WHERE o.customer.id = :customerId AND o.isActive = true AND " +
            "(:status IS NULL OR o.status = :status) AND " +
            "(:fromDate IS NULL OR o.orderDate >= :fromDate) AND " +
            "(:toDate IS NULL OR o.orderDate <= :toDate)")
    Page<Order> findCustomerOrdersWithFilters(
            @Param("customerId") Integer customerId,
            @Param("status") String status,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate,
            Pageable pageable
    );
}