package com.fooddelivery.DTO.summary;
import com.fooddelivery.Entities.DeliveryDriver;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DriverSummaryDTO {

    private Integer id;
    private String fullName;
    private String phone;
    private String vehicleType;
    private String vehiclePlate;
    private Boolean isOnline;

    public static DriverSummaryDTO fromEntity(DeliveryDriver driver) {
        if (driver == null) {
            return null;
        }

        return new DriverSummaryDTO(
                driver.getId(),
                driver.getFirstName() + " " + driver.getLastName(),
                driver.getPhone(),
                driver.getVehicleType(),
                driver.getVehiclePlate(),
                driver.getIsOnline()
        );
    }
}
