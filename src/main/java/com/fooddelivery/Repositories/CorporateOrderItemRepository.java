package com.fooddelivery.Repositories;

import com.fooddelivery.Entities.CorporateOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CorporateOrderItemRepository extends JpaRepository<CorporateOrderItem, Integer> {

    @Query("SELECT i FROM CorporateOrderItem i WHERE i.id = :id AND i.isActive = true")
    Optional<CorporateOrderItem> findActiveById(@Param("id") Integer id);

    @Query("SELECT i FROM CorporateOrderItem i WHERE i.isActive = true")
    List<CorporateOrderItem> findAllActive();

    @Query("SELECT i FROM CorporateOrderItem i WHERE i.corporateOrder.id = :corporateOrderId AND i.isActive = true")
    List<CorporateOrderItem> findByCorporateOrderId(@Param("corporateOrderId") Integer corporateOrderId);
}