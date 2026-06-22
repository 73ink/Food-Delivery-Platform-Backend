package com.fooddelivery.DTO.response;

import com.fooddelivery.Entities.DeliveryDriver;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DriverResponseDTO {

    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String driverCode;
    private String vehicleType;
    private String vehiclePlate;
    private Double currentLat;
    private Double currentLng;
    private Boolean isOnline;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public static DriverResponseDTO fromEntity(DeliveryDriver driver) {
        if (driver == null) {
            return null;
        }

        return new DriverResponseDTO(
                driver.getId(),
                driver.getFirstName(),
                driver.getLastName(),
                driver.getEmail(),
                driver.getPhone(),
                driver.getDriverCode(),
                driver.getVehicleType(),
                driver.getVehiclePlate(),
                driver.getCurrentLat(),
                driver.getCurrentLng(),
                driver.getIsOnline(),
                driver.getCreatedDate(),
                driver.getUpdatedDate()
        );
    }
}