package com.example.concertManagement_Server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Indicates authentication failed due to invalid user credentials.
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)  // 401
public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}