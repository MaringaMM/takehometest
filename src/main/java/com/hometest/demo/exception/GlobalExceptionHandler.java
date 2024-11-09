package com.hometest.demo.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Global exception handler for handling custom and general exceptions across the application.
 * Provides standardized error responses.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles InsufficientFundsException and returns a BAD_REQUEST response.
     *
     * @param ex The InsufficientFundsException thrown during processing.
     * @return A ResponseEntity with the error message and BAD_REQUEST status.
     */
    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<String> handleInsufficientFunds(InsufficientFundsException ex) {
        log.error("Exception occurred. Insufficient funds", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    /**
     * Handles generic exceptions and returns an INTERNAL_SERVER_ERROR response.
     *
     * @param ex The general Exception thrown during processing.
     * @return A ResponseEntity with a generic error message and INTERNAL_SERVER_ERROR status.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        log.error("Exception occurred. Please investigate", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An internal error occurred.");
    }
}

