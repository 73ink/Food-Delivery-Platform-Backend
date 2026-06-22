package com.fooddelivery.Entities;

import jakarta.persistence.*;
import lombok.*;

// OrderItem stores items added inside a customer order.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_items")
public class OrderItem extends BaseEntity {

    private Integer quantity;

    private Double unitPrice;

    private Double totalPrice;

    private String specialInstructions;

    // Many order items belong to one order.
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // Many order items refer to one menu item.
    @ManyToOne
    @JoinColumn(name = "menu_item_id", nullable = false)
    private MenuItem menuItem;
}