package com.example.concertManagement_Server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

/**
 * View object for Event, exposing only safe fields.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {
    private Long id;
    private LocalDate eventDate;
    private Double ticketPrice;
    private Integer availableTickets;
    private Long venueId;
    private Set<Long> artistIds;
}