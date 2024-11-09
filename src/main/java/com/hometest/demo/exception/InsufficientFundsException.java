package com.hometest.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when an account has insufficient funds for a requested withdrawal.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InsufficientFundsException extends RuntimeException {
    /**
     * Constructs a new InsufficientFundsException with the specified message.
     *
     * @param message The detail message for the exception.
     */
    public InsufficientFundsException(String message) {
        super(message);
    }
}

