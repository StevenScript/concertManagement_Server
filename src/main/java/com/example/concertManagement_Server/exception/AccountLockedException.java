package com.example.concertManagement_Server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when an account is temporarily locked due to too many login failures.
 */
@ResponseStatus(HttpStatus.LOCKED)  // HTTP 423
public class AccountLockedException extends RuntimeException {
    public AccountLockedException(String msg) {
        super(msg);
    }
}