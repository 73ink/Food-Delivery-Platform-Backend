package com.fooddelivery.Services;


import com.fooddelivery.Repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


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


}