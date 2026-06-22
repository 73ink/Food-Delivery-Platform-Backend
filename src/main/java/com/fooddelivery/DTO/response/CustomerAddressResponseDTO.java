package com.fooddelivery.DTO.response;

import com.fooddelivery.Entities.CustomerAddress;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAddressResponseDTO {

    private Integer id;
    private String street;
    private String city;
    private String building;
    private Boolean isDefault;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public static CustomerAddressResponseDTO fromEntity(CustomerAddress address) {
        if (address == null) {
            return null;
        }

        return new CustomerAddressResponseDTO(
                address.getId(),
                address.getStreet(),
                address.getCity(),
                address.getBuilding(),
                address.getIsDefault(),
                address.getCreatedDate(),
                address.getUpdatedDate()
        );
    }
}