package com.fooddelivery.Exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

// This class represents the standard error response returned by the API.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiErrorResponse {

    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;

    // This field is only used for validation errors.
    // Example: {"email": "must be a valid email"}
    private Map<String, String> fieldErrors;
}