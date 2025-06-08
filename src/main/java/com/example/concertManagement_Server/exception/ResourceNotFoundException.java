/**
 * ResourceNotFoundException.java
 *
 * Thrown when a requested entity cannot be found in the database.
 * Typically results in a 404 Not Found error handled by GlobalExceptionHandler.
 *
 * Used by:
 * - All service classes
 * - GlobalExceptionHandler.java
 */
package com.example.concertManagement_Server.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
