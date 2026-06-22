package com.fooddelivery.DTO.request;

import com.fooddelivery.Entities.Customer;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

// Request DTO used when creating a new customer.
@Getter
@Setter
public class CustomerRequestDTO {

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

    // Manual mapper method from DTO to Entity.
    public Customer toEntity() {
        Customer customer = new Customer();
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setEmail(email);
        customer.setPhone(phone);

        // For now, we store the password into passwordHash.
        // Later, this can be replaced with real password encryption.
        customer.setPasswordHash(password);

        customer.setLoyaltyPoints(0);
        return customer;
    }

    // Used for updating existing customer data.
    public void applyTo(Customer customer) {
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setEmail(email);
        customer.setPhone(phone);

        if (password != null && !password.isBlank()) {
            customer.setPasswordHash(password);
        }
    }
}