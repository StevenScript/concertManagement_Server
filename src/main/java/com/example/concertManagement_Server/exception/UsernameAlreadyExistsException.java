/**
 * UsernameAlreadyExistsException.java
 *
 * Thrown when a new user tries to register with a username that is already taken.
 * Typically handled as a 400 Bad Request in GlobalExceptionHandler.
 *
 * Used by:
 * - AuthService.java
 */
package com.example.concertManagement_Server.exception;

public class UsernameAlreadyExistsException extends RuntimeException {
    public UsernameAlreadyExistsException(String message) {
        super(message);
    }
}
