package com.example.concertManagement_Server.repository;

import com.example.concertManagement_Server.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Ticket entities, handling retrieval and aggregate counts.
 */
@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    /**
     * Retrieves all tickets sold for a specific event.
     *
     * @param eventId the ID of the event
     * @return list of tickets
     */
    List<Ticket> findByEventId(Long eventId);

    /**
     * Counts the total tickets sold for a specific event.
     *
     * @param eventId the ID of the event
     * @return number of tickets
     */
    Long countByEventId(Long eventId);

    List<Ticket> findByBuyerEmail(String buyerEmail);
}
