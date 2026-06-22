package com.fooddelivery.DTO.response;

import com.fooddelivery.Entities.Customer;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponseDTO {

    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Integer loyaltyPoints;
    private String customerCode;
    private List<CustomerAddressResponseDTO> addresses;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public static CustomerResponseDTO fromEntity(Customer customer) {
        if (customer == null) {
            return null;
        }

        List<CustomerAddressResponseDTO> addressDTOs = null;

        if (customer.getAddresses() != null) {
            addressDTOs = customer.getAddresses()
                    .stream()
                    .filter(address -> Boolean.TRUE.equals(address.getIsActive()))
                    .map(CustomerAddressResponseDTO::fromEntity)
                    .toList();
        }

        return new CustomerResponseDTO(
                customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getLoyaltyPoints(),
                customer.getCustomerCode(),
                addressDTOs,
                customer.getCreatedDate(),
                customer.getUpdatedDate()
        );
    }
}