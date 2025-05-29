package com.example.concertManagement_Server.service;

import com.example.concertManagement_Server.dto.TicketDto;
import com.example.concertManagement_Server.dto.TicketRequest;
import com.example.concertManagement_Server.exception.CapacityExceededException;
import com.example.concertManagement_Server.exception.ResourceNotFoundException;
import com.example.concertManagement_Server.mapper.TicketMapper;
import com.example.concertManagement_Server.model.Event;
import com.example.concertManagement_Server.model.Ticket;
import com.example.concertManagement_Server.repository.EventRepository;
import com.example.concertManagement_Server.repository.TicketRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

/** Manages ticket lifecycle: retrieval, creation, updates, and buyer queries. */
@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final EventRepository  eventRepository;
    private final TicketMapper     ticketMapper;

    public TicketService(TicketRepository ticketRepository,
                         EventRepository  eventRepository,
                         TicketMapper     ticketMapper) {
        this.ticketRepository = ticketRepository;
        this.eventRepository  = eventRepository;
        this.ticketMapper     = ticketMapper;
    }

    /* ---------- single ticket ---------- */

    public TicketDto getTicketById(Long id) {
        Ticket t = ticketRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Ticket id " + id + " not found"));
        return ticketMapper.toDto(t);
    }

    /* ---------- create ---------- */

    public TicketDto createTicket(TicketRequest req) {

        Event ev = eventRepository.findById(req.getEventId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Event id " + req.getEventId() + " not found"));

        long sold = ticketRepository.countByEventId(ev.getId());
        if (sold >= ev.getAvailableTickets()) {
            throw new CapacityExceededException("No tickets left for event \"" + ev.getName() + '"');
        }

        Ticket t = new Ticket();
        t.setEvent(ev);
        t.setBuyerEmail(resolveBuyerEmail(req.getBuyerEmail()));

        Ticket saved = ticketRepository.save(t);
        return ticketMapper.toDto(saved);
    }

    /* ---------- update ---------- */

    public TicketDto updateTicket(Long id, TicketRequest req) {
        Ticket t = ticketRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Ticket id " + id + " not found"));

        if (req.getEventId() != null) {
            Event ev = eventRepository.findById(req.getEventId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Event id " + req.getEventId() + " not found"));
            t.setEvent(ev);
        }
        if (req.getBuyerEmail() != null) {
            t.setBuyerEmail(req.getBuyerEmail());
        }

        Ticket saved = ticketRepository.save(t);
        return ticketMapper.toDto(saved);
    }

    /* ---------- list by buyer ---------- */

    public List<TicketDto> findByBuyerEmail(String email) {
        return ticketRepository.findByBuyerEmail(email)
                .stream()
                .map(ticketMapper::toDto)
                .toList();
    }

    /* ---------- helpers ---------- */

    private String resolveBuyerEmail(String fromRequest) {
        if (fromRequest != null && !fromRequest.isBlank()) {
            return fromRequest;
        }
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    /* ---------- admin helpers ---------- */

    public List<TicketDto> getAllTickets() {
        return ticketRepository.findAll()
                .stream()
                .map(ticketMapper::toDto)
                .toList();
    }

    public void deleteTicket(Long id) {
        if (!ticketRepository.existsById(id)) {
            throw new ResourceNotFoundException("Ticket id " + id + " not found");
        }
        ticketRepository.deleteById(id);
    }
}
