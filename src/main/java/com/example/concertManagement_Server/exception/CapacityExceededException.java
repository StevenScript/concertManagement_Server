package com.example.concertManagement_Server.exception;

/** Thrown when a ticket or event would exceed the venue’s capacity. */
public class CapacityExceededException extends RuntimeException {
    public CapacityExceededException(String message) {
        super(message);
    }
}
