package com.example.concertManagement_Server.service;

import com.example.concertManagement_Server.dto.TicketDto;
import com.example.concertManagement_Server.dto.TicketRequest;
import com.example.concertManagement_Server.exception.ResourceNotFoundException;
import com.example.concertManagement_Server.model.Event;
import com.example.concertManagement_Server.model.Ticket;
import com.example.concertManagement_Server.repository.EventRepository;
import com.example.concertManagement_Server.repository.TicketRepository;
import org.springframework.stereotype.Service;

/**
 * Manages ticket lifecycle: retrieval, creation, and updates.
 */
@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final EventRepository eventRepository;

    public TicketService(TicketRepository ticketRepository,
                         EventRepository eventRepository) {
        this.ticketRepository = ticketRepository;
        this.eventRepository = eventRepository;
    }

    /**
     * Retrieves a ticket by ID and converts it to DTO.
     */
    public TicketDto getTicketById(Long id) {
        Ticket t = ticketRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Ticket with id " + id + " not found"
                        )
                );
        return toDto(t);
    }

    /**
     * Creates a new ticket from a request DTO.
     */
    public TicketDto createTicket(TicketRequest req) {
        Event ev = eventRepository.findById(req.getEventId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Event with id " + req.getEventId() + " not found"
                        )
                );

        Ticket t = new Ticket();
        t.setEvent(ev);
        t.setSeatNumber(req.getSeatNumber());
        t.setTicketType(req.getTicketType());
        t.setBuyerName(req.getBuyerName());

        Ticket saved = ticketRepository.save(t);
        return toDto(saved);
    }

    /**
     * Updates an existing ticket using the provided DTO.
     */
    public TicketDto updateTicket(Long id, TicketRequest req) {
        Ticket t = ticketRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Ticket with id " + id + " not found"
                        )
                );

        if (req.getEventId() != null) {
            Event ev = eventRepository.findById(req.getEventId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Event with id " + req.getEventId() + " not found"
                            )
                    );
            t.setEvent(ev);
        }
        t.setSeatNumber(req.getSeatNumber());
        t.setTicketType(req.getTicketType());
        t.setBuyerName(req.getBuyerName());

        Ticket saved = ticketRepository.save(t);
        return toDto(saved);
    }

    private TicketDto toDto(Ticket t) {
        return new TicketDto(
                t.getId(),
                t.getEvent().getId(),
                t.getSeatNumber(),
                t.getTicketType(),
                t.getBuyerName()
        );
    }
}