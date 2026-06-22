package com.fooddelivery.DTO.response;

import com.fooddelivery.DTO.summary.CustomerSummaryDTO;
import com.fooddelivery.DTO.summary.DeliverySummaryDTO;
import com.fooddelivery.DTO.summary.PaymentSummaryDTO;
import com.fooddelivery.DTO.summary.RestaurantSummaryDTO;
import com.fooddelivery.Entities.Order;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {

    private Integer id;
    private String orderCode;
    private LocalDateTime orderDate;
    private String status;
    private Double subtotal;
    private Double deliveryFee;
    private Double discountAmount;
    private Double totalAmount;
    private String deliveryNotes;
    private CustomerSummaryDTO customer;
    private RestaurantSummaryDTO restaurant;
    private List<OrderItemResponseDTO> items;
    private DeliverySummaryDTO delivery;
    private PaymentSummaryDTO payment;

    public static OrderResponseDTO fromEntity(Order order) {
        if (order == null) {
            return null;
        }

        List<OrderItemResponseDTO> itemDTOs = null;

        if (order.getOrderItems() != null) {
            itemDTOs = order.getOrderItems()
                    .stream()
                    .filter(item -> Boolean.TRUE.equals(item.getIsActive()))
                    .map(OrderItemResponseDTO::fromEntity)
                    .toList();
        }

        return new OrderResponseDTO(
                order.getId(),
                order.getOrderCode(),
                order.getOrderDate(),
                order.getStatus(),
                order.getSubtotal(),
                order.getDeliveryFee(),
                order.getDiscountAmount(),
                order.getTotalAmount(),
                order.getDeliveryNotes(),
                CustomerSummaryDTO.fromEntity(order.getCustomer()),
                RestaurantSummaryDTO.fromEntity(order.getRestaurant()),
                itemDTOs,
                DeliverySummaryDTO.fromEntity(order.getDelivery()),
                PaymentSummaryDTO.fromEntity(order.getPayment())
        );
    }
}