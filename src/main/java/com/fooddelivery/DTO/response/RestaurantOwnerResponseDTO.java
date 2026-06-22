package com.fooddelivery.DTO.response;

import com.fooddelivery.Entities.RestaurantOwner;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantOwnerResponseDTO {

    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String businessLicenseCode;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public static RestaurantOwnerResponseDTO fromEntity(RestaurantOwner owner) {
        if (owner == null) {
            return null;
        }

        return new RestaurantOwnerResponseDTO(
                owner.getId(),
                owner.getFirstName(),
                owner.getLastName(),
                owner.getEmail(),
                owner.getPhone(),
                owner.getBusinessLicenseCode(),
                owner.getCreatedDate(),
                owner.getUpdatedDate()
        );
    }
}