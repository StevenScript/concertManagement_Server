/**
 * GlobalExceptionHandler.java
 *
 * Centralized handler for all exceptions thrown across the application.
 * Converts exceptions into structured JSON responses with meaningful status codes.
 *
 * Covers:
 * - 404 Not Found
 * - 400 Bad Request
 * - 423 Locked (account lockout)
 * - 500 Internal Server Error fallback
 *
 * Used by: All controller layers
 */
package com.example.concertManagement_Server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    /* ─────────────── 404 ─────────────── */

    /**
     * Handles cases where a requested resource cannot be found.
     *
     * @param ex the thrown exception
     * @return 404 Not Found JSON response
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFound(ResourceNotFoundException ex) {
        Map<String, Object> body = Map.of(
                "timestamp", LocalDateTime.now(),
                "status", HttpStatus.NOT_FOUND.value(),
                "error", "Not Found",
                "message", ex.getMessage()
        );
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    /* ─────────────── 400 ─────────────── */

    /**
     * Handles violations where an operation exceeds a venue's capacity.
     *
     * @param ex the thrown exception
     * @return 400 Bad Request JSON response
     */
    @ExceptionHandler(CapacityExceededException.class)
    public ResponseEntity<Map<String, Object>> handleCapacityExceeded(CapacityExceededException ex) {
        Map<String, Object> body = Map.of(
                "timestamp", LocalDateTime.now(),
                "status", HttpStatus.BAD_REQUEST.value(),
                "error", "Bad Request",
                "message", ex.getMessage()
        );
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    /* ─────────────── 423 ─────────────── */

    /**
     * Handles login lockout scenarios after repeated failed attempts.
     *
     * @param ex the thrown exception
     * @return 423 Locked JSON response
     */
    @ExceptionHandler(AccountLockedException.class)
    public ResponseEntity<Map<String, Object>> handleLocked(AccountLockedException ex) {
        Map<String, Object> body = Map.of(
                "timestamp", LocalDateTime.now(),
                "status", HttpStatus.LOCKED.value(),
                "error", "Account Locked",
                "message", ex.getMessage()
        );
        return new ResponseEntity<>(body, HttpStatus.LOCKED);
    }

    /* ─────────────── 500 ─────────────── */

    /**
     * Handles all uncaught runtime exceptions.
     *
     * @param ex the thrown exception
     * @return 500 Internal Server Error JSON response
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleAllExceptions(Exception ex) {
        Map<String, Object> body = Map.of(
                "timestamp", LocalDateTime.now(),
                "status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "error", "Internal Server Error",
                "message", ex.getMessage()
        );
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
