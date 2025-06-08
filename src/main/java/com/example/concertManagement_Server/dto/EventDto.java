/**
 * EventDto.java
 *
 * A Data Transfer Object used to expose event information to clients.
 * This version intentionally avoids full object nesting and instead uses
 * IDs for associated entities (venue and artists).
 *
 * Used by:
 * - EventController.java (GET requests)
 * - VenueController.java (venue-related lookups)
 */
package com.example.concertManagement_Server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {

    /**
     * Unique identifier of the event.
     */
    private Long id;

    /**
     * Display name of the event.
     */
    private String name;

    /**
     * Scheduled date of the event.
     */
    private LocalDate eventDate;

    /**
     * Ticket price in dollars (or chosen currency).
     */
    private Double ticketPrice;

    /**
     * Total tickets initially available.
     */
    private Integer availableTickets;

    /**
     * Current number of unsold tickets.
     */
    private Long ticketsLeft;

    /**
     * ID of the venue hosting the event.
     */
    private Long venueId;

    /**
     * Set of artist IDs scheduled to perform at the event.
     */
    private Set<Long> artistIds;
}
