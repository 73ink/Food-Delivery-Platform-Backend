package com.fooddelivery.Services;

import com.fooddelivery.DTO.request.DriverRequestDTO;
import com.fooddelivery.DTO.response.DeliveryResponseDTO;
import com.fooddelivery.DTO.response.DriverResponseDTO;
import com.fooddelivery.Entities.Delivery;
import com.fooddelivery.Entities.DeliveryDriver;
import com.fooddelivery.Entities.Order;
import com.fooddelivery.Exceptions.DuplicateResourceException;
import com.fooddelivery.Exceptions.InvalidOrderStateException;
import com.fooddelivery.Exceptions.ResourceNotFoundException;
import com.fooddelivery.Repositories.DeliveryDriverRepository;
import com.fooddelivery.Repositories.DeliveryRepository;
import com.fooddelivery.Repositories.OrderRepository;
import com.fooddelivery.Utils.HelperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

// DeliveryService handles drivers and delivery operations.
@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryDriverRepository deliveryDriverRepository;
    private final OrderRepository orderRepository;

    // Register a new delivery driver.
    @Transactional
    public DriverResponseDTO registerDriver(DriverRequestDTO dto) {
        if (deliveryDriverRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new DuplicateResourceException("Driver email already exists");
        }

        DeliveryDriver driver = dto.toEntity();
        driver.setDriverCode(HelperUtils.generateCode("DRV", 5));

        DeliveryDriver savedDriver = deliveryDriverRepository.save(driver);
        return DriverResponseDTO.fromEntity(savedDriver);
    }

    // Get all active drivers.
    @Transactional(readOnly = true)
    public List<DriverResponseDTO> getAllDrivers() {
        return deliveryDriverRepository.findAllActive()
                .stream()
                .map(DriverResponseDTO::fromEntity)
                .toList();
    }

    // Get all online drivers.
    @Transactional(readOnly = true)
    public List<DriverResponseDTO> getOnlineDrivers() {
        return deliveryDriverRepository.findByIsOnlineTrue()
                .stream()
                .map(DriverResponseDTO::fromEntity)
                .toList();
    }

    // Toggle driver online/offline status.
    @Transactional
    public DriverResponseDTO toggleDriverOnlineStatus(Integer driverId, boolean isOnline) {
        DeliveryDriver driver = findActiveDriver(driverId);

        driver.setIsOnline(isOnline);

        DeliveryDriver savedDriver = deliveryDriverRepository.save(driver);
        return DriverResponseDTO.fromEntity(savedDriver);
    }

    // Update driver location.
    @Transactional
    public DriverResponseDTO updateDriverLocation(Integer driverId, double lat, double lng) {
        DeliveryDriver driver = findActiveDriver(driverId);

        driver.setCurrentLat(lat);
        driver.setCurrentLng(lng);

        DeliveryDriver savedDriver = deliveryDriverRepository.save(driver);
        return DriverResponseDTO.fromEntity(savedDriver);
    }

    // Manually assign a driver to an order.
    @Transactional
    public DeliveryResponseDTO assignDriverToOrder(Integer orderId, Integer driverId) {
        Order order = findActiveOrder(orderId);
        DeliveryDriver driver = findActiveDriver(driverId);

        if (deliveryRepository.findByOrderId(orderId).isPresent()) {
            throw new DuplicateResourceException("This order already has a delivery assigned");
        }

        Delivery delivery = new Delivery();
        delivery.setTrackingCode(HelperUtils.generateCode("TRK", 6));
        delivery.setStatus("ASSIGNED");
        delivery.setAssignedAt(LocalDateTime.now());
        delivery.setOrder(order);
        delivery.setDeliveryDriver(driver);

        order.setStatus("OUT_FOR_DELIVERY");
        orderRepository.save(order);

        Delivery savedDelivery = deliveryRepository.save(delivery);
        return DeliveryResponseDTO.fromEntity(savedDelivery);
    }

    // Automatically assign the first available online driver.
    @Transactional
    public DeliveryResponseDTO autoAssignDriver(Integer orderId) {
        Order order = findActiveOrder(orderId);

        if (deliveryRepository.findByOrderId(orderId).isPresent()) {
            throw new DuplicateResourceException("This order already has a delivery assigned");
        }

        List<DeliveryDriver> onlineDrivers = deliveryDriverRepository.findByIsOnlineTrue();

        DeliveryDriver selectedDriver = null;

        for (DeliveryDriver driver : onlineDrivers) {
            boolean hasActiveDelivery = deliveryRepository.findActiveDeliveryForDriver(driver.getId()).isPresent();

            if (!hasActiveDelivery) {
                selectedDriver = driver;
                break;
            }
        }

        if (selectedDriver == null) {
            throw new ResourceNotFoundException("No available online driver found");
        }

        Delivery delivery = new Delivery();
        delivery.setTrackingCode(HelperUtils.generateCode("TRK", 6));
        delivery.setStatus("ASSIGNED");
        delivery.setAssignedAt(LocalDateTime.now());
        delivery.setOrder(order);
        delivery.setDeliveryDriver(selectedDriver);

        order.setStatus("OUT_FOR_DELIVERY");
        orderRepository.save(order);

        Delivery savedDelivery = deliveryRepository.save(delivery);
        return DeliveryResponseDTO.fromEntity(savedDelivery);
    }

    // Get delivery by ID.
    @Transactional(readOnly = true)
    public DeliveryResponseDTO getDeliveryById(Integer deliveryId) {
        Delivery delivery = findActiveDelivery(deliveryId);
        return DeliveryResponseDTO.fromEntity(delivery);
    }

    // Mark delivery as picked up.
    @Transactional
    public DeliveryResponseDTO markDeliveryPickedUp(Integer deliveryId) {
        Delivery delivery = findActiveDelivery(deliveryId);

        delivery.setStatus("PICKED_UP");
        delivery.setPickedUpAt(LocalDateTime.now());

        if (delivery.getOrder() != null) {
            delivery.getOrder().setStatus("OUT_FOR_DELIVERY");
            orderRepository.save(delivery.getOrder());
        }

        Delivery savedDelivery = deliveryRepository.save(delivery);
        return DeliveryResponseDTO.fromEntity(savedDelivery);
    }

    // Mark delivery as delivered.
    @Transactional
    public DeliveryResponseDTO markDeliveryDelivered(Integer deliveryId) {
        Delivery delivery = findActiveDelivery(deliveryId);

        delivery.setStatus("DELIVERED");
        delivery.setDeliveredAt(LocalDateTime.now());

        if (delivery.getOrder() != null) {
            delivery.getOrder().setStatus("DELIVERED");
            orderRepository.save(delivery.getOrder());
        }

        Delivery savedDelivery = deliveryRepository.save(delivery);
        return DeliveryResponseDTO.fromEntity(savedDelivery);
    }

    // Get deliveries for one driver filtered by status.
    @Transactional(readOnly = true)
    public List<DeliveryResponseDTO> getDeliveriesForDriver(Integer driverId, String status) {
        findActiveDriver(driverId);

        return deliveryRepository.findByDeliveryDriverIdAndStatus(driverId, status)
                .stream()
                .map(DeliveryResponseDTO::fromEntity)
                .toList();
    }

    // Get all delivery history for one driver.
    @Transactional(readOnly = true)
    public List<DeliveryResponseDTO> getDriverDeliveryHistory(Integer driverId) {
        findActiveDriver(driverId);

        return deliveryRepository.findAllActive()
                .stream()
                .filter(delivery -> delivery.getDeliveryDriver() != null)
                .filter(delivery -> delivery.getDeliveryDriver().getId().equals(driverId))
                .map(DeliveryResponseDTO::fromEntity)
                .toList();
    }

    // Get active delivery for one driver.
    @Transactional(readOnly = true)
    public DeliveryResponseDTO getActiveDeliveryForDriver(Integer driverId) {
        findActiveDriver(driverId);

        Delivery delivery = deliveryRepository.findActiveDeliveryForDriver(driverId)
                .orElseThrow(() -> new ResourceNotFoundException("No active delivery found for driver ID: " + driverId));

        return DeliveryResponseDTO.fromEntity(delivery);
    }

    // Get all deliveries by platform status.
    @Transactional(readOnly = true)
    public List<DeliveryResponseDTO> getDeliveriesByStatus(String status) {
        return deliveryRepository.findByStatus(status)
                .stream()
                .map(DeliveryResponseDTO::fromEntity)
                .toList();
    }

    // Get nearby online drivers using HelperUtils.calculateDistance.
    @Transactional(readOnly = true)
    public List<DriverResponseDTO> getNearbyOnlineDrivers(double lat, double lng, double radiusKm) {
        return deliveryDriverRepository.findByIsOnlineTrue()
                .stream()
                .filter(driver -> driver.getCurrentLat() != null && driver.getCurrentLng() != null)
                .filter(driver -> HelperUtils.calculateDistance(
                        lat,
                        lng,
                        driver.getCurrentLat(),
                        driver.getCurrentLng()
                ) <= radiusKm)
                .map(DriverResponseDTO::fromEntity)
                .toList();
    }

    private Delivery findActiveDelivery(Integer deliveryId) {
        return deliveryRepository.findActiveById(deliveryId)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery not found with ID: " + deliveryId));
    }

    private DeliveryDriver findActiveDriver(Integer driverId) {
        return deliveryDriverRepository.findActiveById(driverId)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found with ID: " + driverId));
    }

    private Order findActiveOrder(Integer orderId) {
        return orderRepository.findActiveById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));
    }
}