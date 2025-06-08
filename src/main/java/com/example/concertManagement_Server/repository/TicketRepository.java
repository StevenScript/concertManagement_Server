/**
 * TicketRepository.java
 *
 * Repository for handling persistence and querying of Ticket entities.
 * Provides utilities for:
 * - Finding tickets for a specific event
 * - Counting tickets sold per event
 * - Querying by buyer email for user ticket history
 *
 * Works closely with:
 * - Ticket.java (entity model)
 * - EventRepository.java (via event ID references)
 * - TicketService.java (business logic)
 */
package com.example.concertManagement_Server.repository;

import com.example.concertManagement_Server.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    /**
     * Retrieves all tickets sold for a specific event.
     *
     * @param eventId the ID of the event
     * @return list of associated tickets
     */
    List<Ticket> findByEventId(Long eventId);

    /**
     * Counts how many tickets have been sold for a specific event.
     *
     * @param eventId the ID of the event
     * @return total ticket count
     */
    Long countByEventId(Long eventId);

    /**
     * Finds all tickets purchased by a buyer with the given email.
     *
     * @param buyerEmail the email of the ticket purchaser
     * @return list of matching tickets
     */
    List<Ticket> findByBuyerEmail(String buyerEmail);
}
