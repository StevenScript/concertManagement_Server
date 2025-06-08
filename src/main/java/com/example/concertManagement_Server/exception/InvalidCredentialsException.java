/**
 * InvalidCredentialsException.java
 *
 * Thrown when authentication fails due to incorrect username or password.
 * Automatically maps to HTTP 401 Unauthorized.
 *
 * Used by:
 * - AuthService.java
 * - GlobalExceptionHandler.java
 */
package com.example.concertManagement_Server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED) // 401
public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
