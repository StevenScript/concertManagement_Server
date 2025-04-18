package com.example.concertManagement_Server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

/**
 * Request payload for creating or updating an Event.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventRequest {
    private LocalDate eventDate;
    private Double ticketPrice;
    private Integer availableTickets;
    private Long venueId;
    private Set<Long> artistIds;
}