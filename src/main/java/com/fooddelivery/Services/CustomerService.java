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


}