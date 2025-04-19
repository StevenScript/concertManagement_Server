package com.example.concertManagement_Server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request payload for creating or updating a Ticket.
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
