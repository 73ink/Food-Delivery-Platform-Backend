package com.fooddelivery.Services;


import com.fooddelivery.Repositories.CustomerAddressRepository;
import com.fooddelivery.Repositories.CustomerRepository;
import com.fooddelivery.Repositories.OrderRepository;
import com.fooddelivery.Utils.HelperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;

// CustomerService contains business logic related to customers and addresses.
@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerAddressRepository customerAddressRepository;
    private final OrderRepository orderRepository;


}