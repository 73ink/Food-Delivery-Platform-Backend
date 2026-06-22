package com.fooddelivery.DTO.summary;

import com.fooddelivery.Entities.Delivery;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeliverySummaryDTO {

    private Integer id;
    private String trackingCode;
    private String status;
    private DriverSummaryDTO driver;

    public static DeliverySummaryDTO fromEntity(Delivery delivery) {
        if (delivery == null) {
            return null;
        }

        return new DeliverySummaryDTO(
                delivery.getId(),
                delivery.getTrackingCode(),
                delivery.getStatus(),
                DriverSummaryDTO.fromEntity(delivery.getDeliveryDriver())
        );
    }
}