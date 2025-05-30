package com.example.concertManagement_Server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    /* ---------- 404 ---------- */

    /** Handles cases where a requested resource is not found. */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFound(
            ResourceNotFoundException ex) {

        Map<String, Object> body = Map.of(
                "timestamp", LocalDateTime.now(),
                "status",    HttpStatus.NOT_FOUND.value(),
                "error",     "Not Found",
                "message",   ex.getMessage()
        );
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    /* ---------- 400 ---------- */

    /** Thrown when a ticket purchase or event update exceeds venue capacity. */
    @ExceptionHandler(CapacityExceededException.class)
    public ResponseEntity<Map<String, Object>> handleCapacityExceeded(
            CapacityExceededException ex) {

        Map<String, Object> body = Map.of(
                "timestamp", LocalDateTime.now(),
                "status",    HttpStatus.BAD_REQUEST.value(),
                "error",     "Bad Request",
                "message",   ex.getMessage()
        );
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    /* ---------- 500 fallback ---------- */

    /** Catches any uncaught exceptions and returns a generic 500 response. */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleAllExceptions(Exception ex) {
        Map<String, Object> body = Map.of(
                "timestamp", LocalDateTime.now(),
                "status",    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "error",     "Internal Server Error",
                "message",   ex.getMessage()
        );
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AccountLockedException.class)
    public ResponseEntity<Map<String,Object>> handleLocked(AccountLockedException ex) {
        Map<String,Object> body = Map.of(
                "timestamp", LocalDateTime.now(),
                "status", HttpStatus.LOCKED.value(),
                "error", "Account Locked",
                "message", ex.getMessage()
        );
        return new ResponseEntity<>(body, HttpStatus.LOCKED);
    }
}
