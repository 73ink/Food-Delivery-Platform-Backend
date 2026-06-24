package com.fooddelivery.Services;

import com.fooddelivery.DTO.request.CustomerAddressRequestDTO;
import com.fooddelivery.DTO.request.CustomerPatchDTO;
import com.fooddelivery.DTO.request.CustomerRequestDTO;
import com.fooddelivery.DTO.response.CustomerAddressResponseDTO;
import com.fooddelivery.DTO.response.CustomerResponseDTO;
import com.fooddelivery.DTO.response.OrderResponseDTO;
import com.fooddelivery.Entities.Customer;
import com.fooddelivery.Entities.CustomerAddress;
import com.fooddelivery.Exceptions.DuplicateResourceException;
import com.fooddelivery.Exceptions.ResourceNotFoundException;
import com.fooddelivery.Repositories.CustomerAddressRepository;
import com.fooddelivery.Repositories.CustomerRepository;
import com.fooddelivery.Repositories.OrderRepository;
import com.fooddelivery.Utils.HelperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// CustomerService contains business logic related to customers and addresses.
@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerAddressRepository customerAddressRepository;
    private final OrderRepository orderRepository;

    // Create customer without initial address.
    @Transactional
    public CustomerResponseDTO createCustomer(CustomerRequestDTO dto) {

        // Check if email already exists for an active customer.
        if (customerRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new DuplicateResourceException("Customer email already exists");
        }

        Customer customer = dto.toEntity();

        // Generate customer code using overloaded HelperUtils method.
        customer.setCustomerCode(HelperUtils.generateCode("CUST"));

        Customer savedCustomer = customerRepository.save(customer);

        return CustomerResponseDTO.fromEntity(savedCustomer);
    }

    // Create customer with initial address.
    @Transactional
    public CustomerResponseDTO createCustomer(CustomerRequestDTO dto, CustomerAddressRequestDTO initialAddress) {

        if (customerRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new DuplicateResourceException("Customer email already exists");
        }

        Customer customer = dto.toEntity();
        customer.setCustomerCode(HelperUtils.generateCode("CUST"));

        CustomerAddress address = initialAddress.toEntity();
        address.setCustomer(customer);
        address.setIsDefault(true);

        customer.getAddresses().add(address);

        Customer savedCustomer = customerRepository.save(customer);

        return CustomerResponseDTO.fromEntity(savedCustomer);
    }

    // Get all active customers.
    @Transactional(readOnly = true)
    public List<CustomerResponseDTO> getAllCustomers() {
        return customerRepository.findAllActive()
                .stream()
                .map(CustomerResponseDTO::fromEntity)
                .toList();
    }

    // Get customer by ID.
    @Transactional(readOnly = true)
    public CustomerResponseDTO getCustomerById(Integer customerId) {
        Customer customer = findActiveCustomer(customerId);
        return CustomerResponseDTO.fromEntity(customer);
    }

    // Get customer by email.
    @Transactional(readOnly = true)
    public CustomerResponseDTO getCustomerByEmail(String email) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with email: " + email));

        return CustomerResponseDTO.fromEntity(customer);
    }

    // Add a new address to a customer.
    @Transactional
    public CustomerAddressResponseDTO addAddress(Integer customerId, CustomerAddressRequestDTO dto) {
        Customer customer = findActiveCustomer(customerId);

        // If the new address is default, remove default from old addresses.
        if (Boolean.TRUE.equals(dto.getIsDefault())) {
            List<CustomerAddress> oldAddresses = customerAddressRepository.findByCustomerId(customerId);

            for (CustomerAddress address : oldAddresses) {
                address.setIsDefault(false);
                customerAddressRepository.save(address);
            }
        }

        CustomerAddress address = dto.toEntity();
        address.setCustomer(customer);

        CustomerAddress savedAddress = customerAddressRepository.save(address);

        return CustomerAddressResponseDTO.fromEntity(savedAddress);
    }

    // Get all addresses for a customer.
    @Transactional(readOnly = true)
    public List<CustomerAddressResponseDTO> getAddressesForCustomer(Integer customerId) {
        findActiveCustomer(customerId);

        return customerAddressRepository.findByCustomerId(customerId)
                .stream()
                .map(CustomerAddressResponseDTO::fromEntity)
                .toList();
    }

    // Set one address as default.
    @Transactional
    public CustomerAddressResponseDTO setDefaultAddress(Integer addressId) {
        CustomerAddress selectedAddress = customerAddressRepository.findActiveById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with ID: " + addressId));

        Integer customerId = selectedAddress.getCustomer().getId();

        List<CustomerAddress> addresses = customerAddressRepository.findByCustomerId(customerId);

        for (CustomerAddress address : addresses) {
            address.setIsDefault(false);
            customerAddressRepository.save(address);
        }

        selectedAddress.setIsDefault(true);

        CustomerAddress savedAddress = customerAddressRepository.save(selectedAddress);

        return CustomerAddressResponseDTO.fromEntity(savedAddress);
    }

    // Soft delete customer address.
    @Transactional
    public void deleteAddress(Integer addressId) {
        CustomerAddress address = customerAddressRepository.findActiveById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with ID: " + addressId));

        address.setIsActive(false);
        customerAddressRepository.save(address);
    }

    // Add loyalty points.
    @Transactional
    public CustomerResponseDTO updateLoyaltyPoints(Integer customerId, int points) {
        Customer customer = findActiveCustomer(customerId);

        int currentPoints = customer.getLoyaltyPoints() != null ? customer.getLoyaltyPoints() : 0;
        customer.setLoyaltyPoints(currentPoints + points);

        Customer savedCustomer = customerRepository.save(customer);

        return CustomerResponseDTO.fromEntity(savedCustomer);
    }

    // Deduct loyalty points.
    @Transactional
    public CustomerResponseDTO applyLoyaltyPenalty(Integer customerId, int pointsDeducted) {
        Customer customer = findActiveCustomer(customerId);

        int currentPoints = customer.getLoyaltyPoints() != null ? customer.getLoyaltyPoints() : 0;
        int newPoints = Math.max(0, currentPoints - pointsDeducted);

        customer.setLoyaltyPoints(newPoints);

        Customer savedCustomer = customerRepository.save(customer);

        return CustomerResponseDTO.fromEntity(savedCustomer);
    }


}