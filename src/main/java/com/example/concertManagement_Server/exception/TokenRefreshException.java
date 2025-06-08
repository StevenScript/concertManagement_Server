/**
 * TokenRefreshException.java
 *
 * Thrown when a provided refresh token is expired, revoked, or otherwise invalid.
 * Automatically maps to HTTP 401 Unauthorized.
 *
 * Used by:
 * - AuthService.java
 */
package com.example.concertManagement_Server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED) // 401
public class TokenRefreshException extends RuntimeException {
    public TokenRefreshException(String message) {
        super(message);
    }
}
