package com.example.concertManagement_Server.service;

import com.example.concertManagement_Server.exception.ResourceNotFoundException;
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

    // Retrieves all tickets from the database
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    // Retrieves a ticket by its ID, or returns null if not found
    public Ticket getTicketById(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket with id " + id + " not found"));
    }

    // Creates and saves a new ticket
    public Ticket createTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    // Updates an existing ticket if found
    public Ticket updateTicket(Long id, Ticket updatedData) {
        return ticketRepository.findById(id).map(t -> {
            t.setBuyerName(updatedData.getBuyerName());
            t.setTicketType(updatedData.getTicketType());
            t.setSeatNumber(updatedData.getSeatNumber());
            return ticketRepository.save(t);
        }).orElseThrow(() -> new ResourceNotFoundException("Ticket with id " + id + " not found"));
    }
}
