package com.fooddelivery.DTO.request;

import com.fooddelivery.Entities.RestaurantOwner;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

// Request DTO used when creating a restaurant owner.
@Getter
@Setter
public class RestaurantOwnerRequestDTO {

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

    @NotBlank(message = "Business license code is required")
    private String businessLicenseCode;

    public RestaurantOwner toEntity() {
        RestaurantOwner owner = new RestaurantOwner();
        owner.setFirstName(firstName);
        owner.setLastName(lastName);
        owner.setEmail(email);
        owner.setPhone(phone);
        owner.setPasswordHash(password);
        owner.setBusinessLicenseCode(businessLicenseCode);
        return owner;
    }
}
//Request DTO used when creating a restaurant owner.