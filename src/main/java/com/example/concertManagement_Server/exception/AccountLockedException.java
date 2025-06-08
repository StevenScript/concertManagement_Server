/**
 * AccountLockedException.java
 *
 * Thrown when a user’s account is temporarily locked due to repeated failed login attempts.
 * Automatically returns an HTTP 423 (LOCKED) response.
 *
 * Used by:
 * - AuthService.java
 * - GlobalExceptionHandler.java
 */
package com.example.concertManagement_Server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.LOCKED) // HTTP 423
public class AccountLockedException extends RuntimeException {
    public AccountLockedException(String msg) {
        super(msg);
    }
}
