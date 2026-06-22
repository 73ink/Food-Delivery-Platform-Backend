package com.fooddelivery.DTO.request;

import com.fooddelivery.Entities.CorporateOrder;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

// Request DTO used when creating a corporate order.
@Getter
@Setter
public class CorporateOrderRequestDTO {

    @NotNull(message = "Restaurant ID is required")
    private Integer restaurantId;

    @NotBlank(message = "Company name is required")
    private String companyName;

    @NotBlank(message = "Cost center is required")
    private String costCenter;

    @NotEmpty(message = "Corporate order must contain at least one item")
    @Valid
    private List<CorporateOrderItemRequestDTO> items;

    public CorporateOrder toEntity() {
        CorporateOrder order = new CorporateOrder();
        order.setCompanyName(companyName);
        order.setCostCenter(costCenter);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING");
        order.setTotalAmount(0.0);
        return order;
    }
}