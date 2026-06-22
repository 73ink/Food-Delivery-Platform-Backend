package com.fooddelivery.DTO.request;

import com.fooddelivery.Entities.Customer;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

// Request DTO used for partial customer update.
// Fields are optional because PATCH updates only provided values.
@Getter
@Setter
public class CustomerPatchDTO {

    private String firstName;

    private String lastName;

    @Pattern(regexp = "^\\+?[0-9]{8,15}$", message = "Phone number must be 8 to 15 digits")
    private String phone;

    public void applyTo(Customer customer) {
        if (firstName != null) {
            customer.setFirstName(firstName);
        }

        if (lastName != null) {
            customer.setLastName(lastName);
        }

        if (phone != null) {
            customer.setPhone(phone);
        }
    }
}