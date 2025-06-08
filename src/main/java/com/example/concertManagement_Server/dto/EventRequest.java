/**
 * EventRequest.java
 *
 * Payload object used to create or update events via the API.
 * References venue and artist relationships by their IDs.
 *
 * Used by:
 * - EventController.java (POST, PUT endpoints)
 * - EventService.java (entity creation and update)
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
public class EventRequest {

    /**
     * Name or title of the event.
     */
    private String name;

    /**
     * Date the event is scheduled for.
     */
    private LocalDate eventDate;

    /**
     * Ticket price for the event.
     */
    private Double ticketPrice;

    /**
     * Number of tickets available when the event is created.
     */
    private Integer availableTickets;

    /**
     * ID of the venue where the event takes place.
     */
    private Long venueId;

    /**
     * Set of artist IDs performing at the event.
     */
    private Set<Long> artistIds;
}
