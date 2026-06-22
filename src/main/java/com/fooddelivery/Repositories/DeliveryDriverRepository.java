package com.fooddelivery.Repositories;

import com.fooddelivery.Entities.DeliveryDriver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DeliveryDriverRepository extends JpaRepository<DeliveryDriver, Integer> {

    @Query("SELECT d FROM DeliveryDriver d WHERE d.id = :id AND d.isActive = true")
    Optional<DeliveryDriver> findActiveById(@Param("id") Integer id);

    @Query("SELECT d FROM DeliveryDriver d WHERE d.isActive = true")
    List<DeliveryDriver> findAllActive();

    @Query("SELECT d FROM DeliveryDriver d WHERE d.email = :email AND d.isActive = true")
    Optional<DeliveryDriver> findByEmail(@Param("email") String email);

    @Query("SELECT d FROM DeliveryDriver d WHERE d.isOnline = true AND d.isActive = true")
    List<DeliveryDriver> findByIsOnlineTrue();
}