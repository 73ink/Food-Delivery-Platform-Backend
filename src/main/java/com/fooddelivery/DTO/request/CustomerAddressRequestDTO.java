package com.fooddelivery.DTO.request;

import com.fooddelivery.Entities.CustomerAddress;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

// Request DTO used when adding a customer address.
@Getter
@Setter
public class CustomerAddressRequestDTO {

    @NotBlank(message = "Street is required")
    private String street;

    @NotBlank(message = "City is required")
    private String city;

    private String building;

    private Boolean isDefault = false;

    public CustomerAddress toEntity() {
        CustomerAddress address = new CustomerAddress();
        address.setStreet(street);
        address.setCity(city);
        address.setBuilding(building);
        address.setIsDefault(isDefault != null ? isDefault : false);
        return address;
    }

    public void applyTo(CustomerAddress address) {
        address.setStreet(street);
        address.setCity(city);
        address.setBuilding(building);
        address.setIsDefault(isDefault != null ? isDefault : false);
    }
}