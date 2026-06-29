package com.fooddelivery.Exceptions;

// Used when an order action is not allowed.
// Example: cancelling an order that is already delivered.
public class InvalidOrderStateException extends RuntimeException {

    public InvalidOrderStateException(String message) {
        super(message);
    }
}