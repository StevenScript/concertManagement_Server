package com.example.concertManagement_Server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Payload for creating or updating a ticket.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketRequest {
    private Long eventId;
    private String seatNumber;
    private String ticketType;
    private String buyerName;
}
