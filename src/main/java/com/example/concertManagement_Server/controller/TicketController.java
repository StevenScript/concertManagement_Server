package com.example.concertManagement_Server.controller;

import com.example.concertManagement_Server.dto.TicketDto;
import com.example.concertManagement_Server.dto.TicketRequest;
import com.example.concertManagement_Server.exception.ResourceNotFoundException;
import com.example.concertManagement_Server.service.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    /**
     * Retrieves a ticket by ID, responds 404 if not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TicketDto> getTicket(@PathVariable Long id) {
        try {
            TicketDto dto = ticketService.getTicketById(id);
            return ResponseEntity.ok(dto);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Creates a new ticket and returns it with 201 status.
     */
    @PostMapping
    public ResponseEntity<TicketDto> createTicket(@RequestBody TicketRequest req) {
        TicketDto dto = ticketService.createTicket(req);
        return ResponseEntity.status(201).body(dto);
    }

    /**
     * Updates an existing ticket, responds 404 if not found.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TicketDto> updateTicket(
            @PathVariable Long id,
            @RequestBody TicketRequest req) {
        try {
            TicketDto dto = ticketService.updateTicket(id, req);
            return ResponseEntity.ok(dto);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/buyer/{email}")
    public List<TicketDto> ticketsForBuyer(@PathVariable String email) {
        return ticketService.findByBuyerEmail(email);
    }

    @GetMapping("/me")
    public List<TicketDto> myTickets() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ticketService.findByBuyerEmail(email);
    }

    @GetMapping
    public List<TicketDto> getAllTickets() {
        return ticketService.getAllTickets();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        try {
            ticketService.deleteTicket(id);
            return ResponseEntity.noContent().build();   // 204
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();    // 404
        }
    }
}