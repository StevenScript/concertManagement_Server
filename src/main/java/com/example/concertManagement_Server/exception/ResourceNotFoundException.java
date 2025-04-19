package com.example.concertManagement_Server.exception;


/**
 * Thrown when an entity cannot be found in the data store.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}