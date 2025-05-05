package com.example.concertManagement_Server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

/**
 * DTO for exposing event details to clients.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {
    private Long id;
    private String name;
    private LocalDate eventDate;
    private Double ticketPrice;
    private Integer availableTickets;
    private Long venueId;
    private Set<Long> artistIds;
}