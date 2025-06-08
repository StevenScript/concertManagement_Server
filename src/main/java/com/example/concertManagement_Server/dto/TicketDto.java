/**
 * TicketDto.java
 *
 * A response DTO representing a purchased ticket.
 * Intended for API responses where full entity exposure is unnecessary.
 * Includes event summary, venue, and buyer information.
 *
 * Used by:
 * - TicketController.java (GET requests)
 * - User and event queries exposing ticket data
 */
package com.example.concertManagement_Server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketDto {

    /**
     * Unique identifier of the ticket.
     */
    private Long id;

    /**
     * Name of the event associated with this ticket.
     */
    private String eventName;

    /**
     * Date the associated event takes place.
     */
    private LocalDate eventDate;

    /**
     * Venue where the event is hosted.
     */
    private String venueName;

    /**
     * Email of the buyer who purchased the ticket.
     */
    private String buyerEmail;
}
