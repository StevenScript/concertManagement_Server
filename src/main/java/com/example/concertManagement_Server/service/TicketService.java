package com.example.concertManagement_Server.service;

import com.example.concertManagement_Server.model.Ticket;
import com.example.concertManagement_Server.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public Ticket getTicketById(Long id) {
        Optional<Ticket> optional = ticketRepository.findById(id);
        return optional.orElse(null);
    }
}
