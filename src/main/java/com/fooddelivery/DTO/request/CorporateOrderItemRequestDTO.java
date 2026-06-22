package com.fooddelivery.DTO.request;

import com.fooddelivery.Entities.CorporateOrderItem;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

// Request DTO used for each item inside a corporate order.
@Getter
@Setter
public class CorporateOrderItemRequestDTO {

    @NotNull(message = "Menu item ID is required")
    private Integer menuItemId;

    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    private String specialInstructions;

    public CorporateOrderItem toEntity() {
        CorporateOrderItem item = new CorporateOrderItem();
        item.setQuantity(quantity);
        item.setSpecialInstructions(specialInstructions);
        return item;
    }
}