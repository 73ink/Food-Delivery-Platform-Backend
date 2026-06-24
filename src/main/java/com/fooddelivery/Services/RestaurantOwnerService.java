package com.fooddelivery.Services;

import com.fooddelivery.DTO.request.RestaurantOwnerRequestDTO;
import com.fooddelivery.DTO.response.RestaurantOwnerResponseDTO;
import com.fooddelivery.Entities.RestaurantOwner;
import com.fooddelivery.Exceptions.DuplicateResourceException;
import com.fooddelivery.Exceptions.ResourceNotFoundException;
import com.fooddelivery.Repositories.RestaurantOwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// RestaurantOwnerService contains business logic related to restaurant owners.
@Service
@RequiredArgsConstructor
public class RestaurantOwnerService {

    private final RestaurantOwnerRepository restaurantOwnerRepository;

    // Create a new restaurant owner.
    @Transactional
    public RestaurantOwnerResponseDTO createRestaurantOwner(RestaurantOwnerRequestDTO dto) {

        // Prevent duplicate owner email.
        if (restaurantOwnerRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new DuplicateResourceException("Restaurant owner email already exists");
        }

        RestaurantOwner owner = dto.toEntity();

        RestaurantOwner savedOwner = restaurantOwnerRepository.save(owner);

        return RestaurantOwnerResponseDTO.fromEntity(savedOwner);
    }


}