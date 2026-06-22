package com.fooddelivery.DTO.response;

import com.fooddelivery.Entities.Customer;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSummaryDTO {

    private Integer id;
    private String fullName;
    private String email;
    private String phone;
    private Integer loyaltyPoints;

    public static CustomerSummaryDTO fromEntity(Customer customer) {
        if (customer == null) {
            return null;
        }

        return new CustomerSummaryDTO(
                customer.getId(),
                customer.getFirstName() + " " + customer.getLastName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getLoyaltyPoints()
        );
    }
}