/**
 * TicketRequest.java
 *
 * Payload used to create or update a ticket purchase.
 * Allows either direct buyer email or defaults to the authenticated user.
 *
 * Used by:
 * - TicketController.java (POST and PUT)
 * - TicketService.java (ticket issuance logic)
 */
package com.example.concertManagement_Server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketRequest {

    /**
     * ID of the event for which the ticket is being purchased.
     */
    private Long eventId;

    /**
     * Email of the ticket buyer. If null, the authenticated user's email is used.
     */
    private String buyerEmail;
}
