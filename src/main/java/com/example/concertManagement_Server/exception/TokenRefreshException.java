package com.example.concertManagement_Server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when a refresh token is invalid, expired, or revoked.
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)  // 401
public class TokenRefreshException extends RuntimeException {
    public TokenRefreshException(String message) {
        super(message);
    }
}

