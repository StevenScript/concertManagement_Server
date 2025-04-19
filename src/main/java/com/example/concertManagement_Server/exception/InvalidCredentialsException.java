package com.example.concertManagement_Server.exception;

/**
 * Indicates authentication failed due to invalid user credentials.
 */
public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}