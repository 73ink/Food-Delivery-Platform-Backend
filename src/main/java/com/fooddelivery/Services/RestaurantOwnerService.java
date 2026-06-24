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

    // Get all active restaurant owners.
    @Transactional(readOnly = true)
    public List<RestaurantOwnerResponseDTO> getAllOwners() {
        return restaurantOwnerRepository.findAllActive()
                .stream()
                .map(RestaurantOwnerResponseDTO::fromEntity)
                .toList();
    }

    // Get owner by ID.
    @Transactional(readOnly = true)
    public RestaurantOwnerResponseDTO getOwnerById(Integer ownerId) {
        RestaurantOwner owner = findActiveOwner(ownerId);

        return RestaurantOwnerResponseDTO.fromEntity(owner);
    }

    // Soft delete restaurant owner.
    @Transactional
    public void deactivateOwner(Integer ownerId) {
        RestaurantOwner owner = findActiveOwner(ownerId);

        owner.setIsActive(false);

        restaurantOwnerRepository.save(owner);
    }

    // This helper will also be used later by RestaurantService.
    @Transactional(readOnly = true)
    public RestaurantOwner findOwnerEntityById(Integer ownerId) {
        return findActiveOwner(ownerId);
    }

    // Private helper method for active owner lookup.
    private RestaurantOwner findActiveOwner(Integer ownerId) {
        return restaurantOwnerRepository.findActiveById(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant owner not found with ID: " + ownerId));
    }
}