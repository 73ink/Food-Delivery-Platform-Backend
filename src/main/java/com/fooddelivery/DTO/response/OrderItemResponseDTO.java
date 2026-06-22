package com.fooddelivery.DTO.response;

import com.fooddelivery.Entities.OrderItem;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponseDTO {

    private Integer id;
    private Integer menuItemId;
    private String menuItemName;
    private Integer quantity;
    private Double unitPrice;
    private Double totalPrice;
    private String specialInstructions;

    public static OrderItemResponseDTO fromEntity(OrderItem item) {
        if (item == null) {
            return null;
        }

        return new OrderItemResponseDTO(
                item.getId(),
                item.getMenuItem() != null ? item.getMenuItem().getId() : null,
                item.getMenuItem() != null ? item.getMenuItem().getName() : null,
                item.getQuantity(),
                item.getUnitPrice(),
                item.getTotalPrice(),
                item.getSpecialInstructions()
        );
    }
}