package com.fooddelivery.Repositories;

import com.fooddelivery.Entities.CorporateOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CorporateOrderRepository extends JpaRepository<CorporateOrder, Integer> {

    @Query("SELECT c FROM CorporateOrder c WHERE c.id = :id AND c.isActive = true")
    Optional<CorporateOrder> findActiveById(@Param("id") Integer id);

    @Query("SELECT c FROM CorporateOrder c WHERE c.isActive = true")
    List<CorporateOrder> findAllActive();

    @Query("SELECT c FROM CorporateOrder c WHERE c.restaurant.id = :restaurantId AND c.isActive = true")
    List<CorporateOrder> findByRestaurantId(@Param("restaurantId") Integer restaurantId);

    @Query("SELECT c FROM CorporateOrder c WHERE c.restaurant.id = :restaurantId AND c.status = :status AND c.isActive = true")
    List<CorporateOrder> findByRestaurantIdAndStatus(
            @Param("restaurantId") Integer restaurantId,
            @Param("status") String status
    );
}