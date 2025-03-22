package com.onlinebookstore.interswitch.shared.exception;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Represents an API error response.
 */
@Builder
@Data
public class ApiError {
    /**
     * List of error messages.
     */
    private List<String> errorMessage;

    /**
     * Error code associated with the error.
     */
    private String errorCode;

    /**
     * The request that resulted in this error.
     */
    private String request;

    /**
     * The type of request that resulted in this error (e.g., GET, POST).
     */
    private String requestType;

    /**
     * A custom message associated with the error.
     */
    private String customMessage;
}
