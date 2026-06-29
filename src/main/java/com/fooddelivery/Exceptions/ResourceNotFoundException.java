package com.fooddelivery.Exceptions;

// Used when an entity is not found or when it is soft-deleted.
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}