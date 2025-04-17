package com.example.concertManagement_Server.exception;

/**
 * Thrown when attempting to register a username that already exists in the system.
 */
public class UsernameAlreadyExistsException extends RuntimeException {
    public UsernameAlreadyExistsException(String message) {
        super(message);
    }
}

