package com.example.concertManagement_Server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

/**
 * Payload for creating or updating an event.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventRequest {
    private String name;
    private LocalDate eventDate;
    private Double ticketPrice;
    private Integer availableTickets;
    private Long venueId;
    private Set<Long> artistIds;
}