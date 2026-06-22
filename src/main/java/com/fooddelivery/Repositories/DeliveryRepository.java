package com.fooddelivery.Repositories;

import com.fooddelivery.Entities.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DeliveryRepository extends JpaRepository<Delivery, Integer> {

    @Query("SELECT d FROM Delivery d WHERE d.id = :id AND d.isActive = true")
    Optional<Delivery> findActiveById(@Param("id") Integer id);

    @Query("SELECT d FROM Delivery d WHERE d.isActive = true")
    List<Delivery> findAllActive();

    @Query("SELECT d FROM Delivery d WHERE d.deliveryDriver.id = :driverId AND d.status = :status AND d.isActive = true")
    List<Delivery> findByDeliveryDriverIdAndStatus(
            @Param("driverId") Integer driverId,
            @Param("status") String status
    );

    @Query("SELECT d FROM Delivery d WHERE d.status = :status AND d.isActive = true")
    List<Delivery> findByStatus(@Param("status") String status);

    @Query("SELECT d FROM Delivery d WHERE d.order.id = :orderId AND d.isActive = true")
    Optional<Delivery> findByOrderId(@Param("orderId") Integer orderId);

    @Query("SELECT d FROM Delivery d WHERE d.deliveryDriver.id = :driverId AND d.status <> 'DELIVERED' AND d.isActive = true")
    Optional<Delivery> findActiveDeliveryForDriver(@Param("driverId") Integer driverId);
}