package com.fooddelivery.DTO.response;

import com.fooddelivery.Entities.Delivery;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryResponseDTO {

    private Integer id;
    private String trackingCode;
    private String status;
    private LocalDateTime assignedAt;
    private LocalDateTime pickedUpAt;
    private LocalDateTime deliveredAt;
    private Integer orderId;
    private String orderCode;
    private DriverSummaryDTO driver;

    public static DeliveryResponseDTO fromEntity(Delivery delivery) {
        if (delivery == null) {
            return null;
        }

        return new DeliveryResponseDTO(
                delivery.getId(),
                delivery.getTrackingCode(),
                delivery.getStatus(),
                delivery.getAssignedAt(),
                delivery.getPickedUpAt(),
                delivery.getDeliveredAt(),
                delivery.getOrder() != null ? delivery.getOrder().getId() : null,
                delivery.getOrder() != null ? delivery.getOrder().getOrderCode() : null,
                DriverSummaryDTO.fromEntity(delivery.getDeliveryDriver())
        );
    }
}