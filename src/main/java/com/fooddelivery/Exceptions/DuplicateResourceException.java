package com.fooddelivery.Exceptions;

// Used when trying to create a record that already exists.
// Example: registering a customer with an email that already exists.
public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String message) {
        super(message);
    }
}