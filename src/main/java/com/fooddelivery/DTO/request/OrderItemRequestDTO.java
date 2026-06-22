package com.fooddelivery.DTO.request;

import com.fooddelivery.Entities.OrderItem;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

// Request DTO used when adding an item to an order.
@Getter
@Setter
public class OrderItemRequestDTO {

    @NotNull(message = "Menu item ID is required")
    private Integer menuItemId;

    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    private String specialInstructions;

    public OrderItem toEntity() {
        OrderItem orderItem = new OrderItem();
        orderItem.setQuantity(quantity);
        orderItem.setSpecialInstructions(specialInstructions);
        return orderItem;
    }
}