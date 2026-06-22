package com.fooddelivery.Repositories;

import com.fooddelivery.Entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

    @Query("SELECT i FROM OrderItem i WHERE i.id = :id AND i.isActive = true")
    Optional<OrderItem> findActiveById(@Param("id") Integer id);

    @Query("SELECT i FROM OrderItem i WHERE i.isActive = true")
    List<OrderItem> findAllActive();

    @Query("SELECT i FROM OrderItem i WHERE i.order.id = :orderId AND i.isActive = true")
    List<OrderItem> findByOrderId(@Param("orderId") Integer orderId);

    @Query("SELECT i FROM OrderItem i WHERE i.order.id = :orderId AND i.menuItem.id = :menuItemId AND i.isActive = true")
    Optional<OrderItem> findByOrderIdAndMenuItemId(
            @Param("orderId") Integer orderId,
            @Param("menuItemId") Integer menuItemId
    );
}