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

    // Create order with items and no notes.
    @Transactional
    public OrderResponseDTO createOrder(Integer customerId, Integer restaurantId, List<OrderItemRequestDTO> items) {
        return createOrder(customerId, restaurantId, items, null);
    }

    // Create order with items and delivery notes.
    @Transactional
    public OrderResponseDTO createOrder(
            Integer customerId,
            Integer restaurantId,
            List<OrderItemRequestDTO> items,
            String notes
    ) {
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
        order.setTotalAmount(0.0);
        order.setDeliveryNotes(notes);
        order.setCustomer(customer);
        order.setRestaurant(restaurant);

        Order savedOrder = orderRepository.save(order);

        // Add every requested item to the order.
        for (OrderItemRequestDTO itemDTO : items) {
            addMenuItemEntityToOrder(savedOrder, itemDTO.getMenuItemId(), itemDTO.getQuantity(), itemDTO.getSpecialInstructions());
        }

        calculateOrderTotals(savedOrder.getId());

        Order updatedOrder = findActiveOrder(savedOrder.getId());

        return OrderResponseDTO.fromEntity(updatedOrder);
    }

    // Add a menu item to an existing order.
    @Transactional
    public OrderResponseDTO addMenuItemToOrder(Integer orderId, Integer menuItemId, int quantity) {
        Order order = findActiveOrder(orderId);

        // Items can only be added while order is still pending.
        if (!"PENDING".equalsIgnoreCase(order.getStatus())) {
            throw new InvalidOrderStateException("Items can only be added to a PENDING order");
        }

        addMenuItemEntityToOrder(order, menuItemId, quantity, null);

        calculateOrderTotals(orderId);

        Order updatedOrder = findActiveOrder(orderId);

        return OrderResponseDTO.fromEntity(updatedOrder);
    }

    // Remove an order item using soft delete.
    @Transactional
    public void removeMenuItemFromOrder(Integer orderId, Integer orderItemId) {
        Order order = findActiveOrder(orderId);

        if (!"PENDING".equalsIgnoreCase(order.getStatus())) {
            throw new InvalidOrderStateException("Items can only be removed from a PENDING order");
        }

        OrderItem orderItem = orderItemRepository.findActiveById(orderItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Order item not found with ID: " + orderItemId));

        if (!orderItem.getOrder().getId().equals(order.getId())) {
            throw new InvalidOrderStateException("This item does not belong to the selected order");
        }

        orderItem.setIsActive(false);
        orderItemRepository.save(orderItem);

        calculateOrderTotals(orderId);
    }

    // Apply discount to an order.
    @Transactional
    public OrderResponseDTO applyDiscount(Integer orderId, double discountAmount) {
        Order order = findActiveOrder(orderId);

        if (discountAmount < 0) {
            throw new InvalidOrderStateException("Discount amount cannot be negative");
        }

        if (discountAmount > order.getSubtotal()) {
            throw new InvalidOrderStateException("Discount cannot be greater than subtotal");
        }

        order.setDiscountAmount(discountAmount);

        Order savedOrder = orderRepository.save(order);

        calculateOrderTotals(savedOrder.getId());

        Order updatedOrder = findActiveOrder(orderId);

        return OrderResponseDTO.fromEntity(updatedOrder);
    }



    // Helper method for DeliveryService and PaymentService.
    @Transactional(readOnly = true)
    public Order findOrderEntityById(Integer orderId) {
        return findActiveOrder(orderId);
    }

    // Private helper to add item entity to order.
    private void addMenuItemEntityToOrder(Order order, Integer menuItemId, int quantity, String specialInstructions) {

        if (quantity < 1) {
            throw new InvalidOrderStateException("Quantity must be at least 1");
        }

        MenuItem menuItem = menuItemRepository.findActiveById(menuItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with ID: " + menuItemId));

        if (!Boolean.TRUE.equals(menuItem.getIsAvailable())) {
            throw new InvalidOrderStateException("Menu item is not available: " + menuItem.getName());
        }

        if (!menuItem.getRestaurant().getId().equals(order.getRestaurant().getId())) {
            throw new InvalidOrderStateException("Menu item does not belong to the selected restaurant");
        }

        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setMenuItem(menuItem);
        orderItem.setQuantity(quantity);
        orderItem.setUnitPrice(menuItem.getPrice());
        orderItem.setTotalPrice(menuItem.getPrice() * quantity);
        orderItem.setSpecialInstructions(specialInstructions);

        orderItemRepository.save(orderItem);
    }

    // Private helper method for active order lookup.
    private Order findActiveOrder(Integer orderId) {
        return orderRepository.findActiveById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));
    }

    // Validate allowed order statuses.
    private boolean isValidOrderStatus(String status) {
        return status.equals("PENDING")
                || status.equals("CONFIRMED")
                || status.equals("PREPARING")
                || status.equals("READY")
                || status.equals("OUT_FOR_DELIVERY")
                || status.equals("DELIVERED")
                || status.equals("CANCELLED");
    }
}