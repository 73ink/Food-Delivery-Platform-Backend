package com.fooddelivery.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

// DeliveryDriver stores driver account and location information.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "delivery_drivers")
public class DeliveryDriver extends BaseEntity {

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    private String phone;

    private String passwordHash;

    @Column(unique = true)
    private String driverCode;

    private String vehicleType;

    private String vehiclePlate;

    private Double currentLat;

    private Double currentLng;

    private Boolean isOnline = false;

    // One driver can have many deliveries.
    @OneToMany(mappedBy = "deliveryDriver")
    private List<Delivery> deliveries = new ArrayList<>();
}