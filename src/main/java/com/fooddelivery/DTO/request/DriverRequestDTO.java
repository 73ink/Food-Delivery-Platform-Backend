package com.fooddelivery.DTO.request;

import com.fooddelivery.Entities.DeliveryDriver;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

// Request DTO used when registering a delivery driver.
@Getter
@Setter
public class DriverRequestDTO {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @Pattern(regexp = "^\\+?[0-9]{8,15}$", message = "Phone number must be 8 to 15 digits")
    private String phone;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Vehicle type is required")
    private String vehicleType;

    @NotBlank(message = "Vehicle plate is required")
    private String vehiclePlate;

    private Double currentLat;

    private Double currentLng;

    private Boolean isOnline = false;

    public DeliveryDriver toEntity() {
        DeliveryDriver driver = new DeliveryDriver();
        driver.setFirstName(firstName);
        driver.setLastName(lastName);
        driver.setEmail(email);
        driver.setPhone(phone);
        driver.setPasswordHash(password);
        driver.setVehicleType(vehicleType);
        driver.setVehiclePlate(vehiclePlate);
        driver.setCurrentLat(currentLat);
        driver.setCurrentLng(currentLng);
        driver.setIsOnline(isOnline != null ? isOnline : false);
        return driver;
    }
}