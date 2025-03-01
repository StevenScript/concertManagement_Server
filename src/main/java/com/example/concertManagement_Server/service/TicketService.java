package com.example.concertManagement_Server.service;

import com.example.concertManagement_Server.model.Ticket;
import com.example.concertManagement_Server.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public Ticket getTicketById(Long id) {
        Optional<Ticket> optional = ticketRepository.findById(id);
        return optional.orElse(null);
    }

    public Ticket createTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    public Ticket updateTicket(Long id, Ticket updatedData) {
        return ticketRepository.findById(id).map(t -> {
            t.setBuyerName(updatedData.getBuyerName());
            t.setTicketType(updatedData.getTicketType());
            t.setSeatNumber(updatedData.getSeatNumber());
            return ticketRepository.save(t);
        }).orElse(null);
    }
}
