package com.example.concertManagement_Server.exception;

/**
 * Thrown when user credentials are invalid during login.
 */
public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}

