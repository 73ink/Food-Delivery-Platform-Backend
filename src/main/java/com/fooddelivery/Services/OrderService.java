package com.fooddelivery.Services;

import com.fooddelivery.DTO.request.CorporateOrderItemRequestDTO;
import com.fooddelivery.DTO.request.CorporateOrderRequestDTO;
import com.fooddelivery.DTO.request.OrderItemRequestDTO;
import com.fooddelivery.DTO.response.CorporateOrderResponseDTO;
import com.fooddelivery.DTO.response.OrderResponseDTO;
import com.fooddelivery.Entities.*;
import com.fooddelivery.Exceptions.InvalidOrderStateException;
import com.fooddelivery.Exceptions.ResourceNotFoundException;
import com.fooddelivery.Repositories.*;
import com.fooddelivery.Utils.HelperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

// OrderService contains business logic related to normal orders and corporate orders.
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CorporateOrderRepository corporateOrderRepository;
    private final CorporateOrderItemRepository corporateOrderItemRepository;
    private final CustomerRepository customerRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;

    // Create an empty order.
    // This supports endpoint: POST /api/orders/customer/{customerId}/restaurant/{restaurantId}
    @Transactional
    public OrderResponseDTO createEmptyOrder(Integer customerId, Integer restaurantId) {

        Customer customer = customerRepository.findActiveById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + customerId));

        Restaurant restaurant = restaurantRepository.findActiveById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with ID: " + restaurantId));

        if (!Boolean.TRUE.equals(restaurant.getAcceptingOrders())) {
            throw new InvalidOrderStateException("Restaurant is not accepting orders right now");
        }

        Order order = new Order();
        order.setOrderCode(HelperUtils.generateCode("ORD", 6));
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING");
        order.setSubtotal(0.0);
        order.setDeliveryFee(restaurant.getDeliveryFee());
        order.setDiscountAmount(0.0);
        order.setTotalAmount(restaurant.getDeliveryFee());
        order.setCustomer(customer);
        order.setRestaurant(restaurant);

        Order savedOrder = orderRepository.save(order);

        return OrderResponseDTO.fromEntity(savedOrder);
    }

}