package com.example.concertManagement_Server.repository;

import com.example.concertManagement_Server.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByEventId(Long eventId);
    Long countByEventId(Long eventId);

}
