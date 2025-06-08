/**
 * CapacityExceededException.java
 *
 * Thrown when an event or ticket purchase exceeds the allowed venue capacity.
 * Typically handled as a 400 Bad Request.
 *
 * Used by:
 * - TicketService.java
 * - EventService.java
 * - GlobalExceptionHandler.java
 */
package com.example.concertManagement_Server.exception;

public class CapacityExceededException extends RuntimeException {
    public CapacityExceededException(String message) {
        super(message);
    }
}

