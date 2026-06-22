package com.fooddelivery.Entities;

import jakarta.persistence.*;
import lombok.*;

// CorporateOrderItem stores items inside a corporate order.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "corporate_order_items")
public class CorporateOrderItem extends BaseEntity {

    private Integer quantity;

    private Double unitPrice;

    private Double totalPrice;

    private String specialInstructions;

    // Many corporate order items belong to one corporate order.
    @ManyToOne
    @JoinColumn(name = "corporate_order_id", nullable = false)
    private CorporateOrder corporateOrder;

    // Many corporate order items refer to one menu item.
    @ManyToOne
    @JoinColumn(name = "menu_item_id", nullable = false)
    private MenuItem menuItem;
}