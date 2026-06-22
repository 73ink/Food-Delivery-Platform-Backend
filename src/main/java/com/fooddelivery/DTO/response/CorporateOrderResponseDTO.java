package com.fooddelivery.DTO.response;

import com.fooddelivery.DTO.summary.RestaurantSummaryDTO;
import com.fooddelivery.Entities.CorporateOrder;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CorporateOrderResponseDTO {

    private Integer id;
    private String corporateCode;
    private String companyName;
    private String costCenter;
    private LocalDateTime orderDate;
    private String status;
    private Double totalAmount;
    private RestaurantSummaryDTO restaurant;
    private List<CorporateOrderItemResponseDTO> items;

    public static CorporateOrderResponseDTO fromEntity(CorporateOrder order) {
        if (order == null) {
            return null;
        }

        List<CorporateOrderItemResponseDTO> itemDTOs = null;

        if (order.getCorporateOrderItems() != null) {
            itemDTOs = order.getCorporateOrderItems()
                    .stream()
                    .filter(item -> Boolean.TRUE.equals(item.getIsActive()))
                    .map(CorporateOrderItemResponseDTO::fromEntity)
                    .toList();
        }

        return new CorporateOrderResponseDTO(
                order.getId(),
                order.getCorporateCode(),
                order.getCompanyName(),
                order.getCostCenter(),
                order.getOrderDate(),
                order.getStatus(),
                order.getTotalAmount(),
                RestaurantSummaryDTO.fromEntity(order.getRestaurant()),
                itemDTOs
        );
    }
}