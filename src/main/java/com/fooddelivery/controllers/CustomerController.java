package com.fooddelivery.controllers;

import com.fooddelivery.DTO.request.CustomerAddressRequestDTO;
import com.fooddelivery.DTO.request.CustomerPatchDTO;
import com.fooddelivery.DTO.request.CustomerRequestDTO;
import com.fooddelivery.DTO.response.CustomerAddressResponseDTO;
import com.fooddelivery.DTO.response.CustomerResponseDTO;
import com.fooddelivery.DTO.response.OrderResponseDTO;
import com.fooddelivery.Services.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerResponseDTO> createCustomer(@Valid @RequestBody CustomerRequestDTO dto) {
        CustomerResponseDTO createdCustomer = customerService.createCustomer(dto);
        return ResponseEntity
                .created(URI.create("/api/customers/" + createdCustomer.getId()))
                .body(createdCustomer);
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponseDTO>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> getCustomerById(@PathVariable Integer id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<CustomerResponseDTO> getCustomerByEmail(@PathVariable String email) {
        return ResponseEntity.ok(customerService.getCustomerByEmail(email));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> patchCustomer(
            @PathVariable Integer id,
            @Valid @RequestBody CustomerPatchDTO dto
    ) {
        return ResponseEntity.ok(customerService.patchCustomer(id, dto));
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateCustomer(@PathVariable Integer id) {
        customerService.deactivateCustomer(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/loyalty/add/{points}")
    public ResponseEntity<CustomerResponseDTO> addLoyaltyPoints(
            @PathVariable Integer id,
            @PathVariable int points
    ) {
        return ResponseEntity.ok(customerService.updateLoyaltyPoints(id, points));
    }

    @PutMapping("/{id}/loyalty/deduct/{points}")
    public ResponseEntity<CustomerResponseDTO> deductLoyaltyPoints(
            @PathVariable Integer id,
            @PathVariable int points
    ) {
        return ResponseEntity.ok(customerService.applyLoyaltyPenalty(id, points));
    }

    @PostMapping("/{id}/addresses")
    public ResponseEntity<CustomerAddressResponseDTO> addAddress(
            @PathVariable Integer id,
            @Valid @RequestBody CustomerAddressRequestDTO dto
    ) {
        CustomerAddressResponseDTO createdAddress = customerService.addAddress(id, dto);
        return ResponseEntity
                .created(URI.create("/api/customers/addresses/" + createdAddress.getId()))
                .body(createdAddress);
    }

    @GetMapping("/{id}/addresses")
    public ResponseEntity<List<CustomerAddressResponseDTO>> getAddresses(@PathVariable Integer id) {
        return ResponseEntity.ok(customerService.getAddressesForCustomer(id));
    }

    @PutMapping("/addresses/{addressId}/default")
    public ResponseEntity<CustomerAddressResponseDTO> setDefaultAddress(@PathVariable Integer addressId) {
        return ResponseEntity.ok(customerService.setDefaultAddress(addressId));
    }

    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Integer addressId) {
        customerService.deleteAddress(addressId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/orders")
    public ResponseEntity<List<OrderResponseDTO>> getCustomerOrders(@PathVariable Integer id) {
        return ResponseEntity.ok(customerService.getOrdersForCustomer(id));
    }
}