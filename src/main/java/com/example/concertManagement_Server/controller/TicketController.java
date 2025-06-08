/**
 * TicketController.java
 *
 * REST controller that manages concert ticket operations.
 * Supports full CRUD access, both as an admin and as a buyer (via /me).
 *
 * Endpoints include:
 * - Creating and updating tickets
 * - Fetching individual or all tickets
 * - Looking up tickets by buyer (authenticated or by email)
 * - Deleting tickets
 *
 * Works closely with:
 * - TicketService.java (business logic and validation)
 * - TicketDto / TicketRequest (data transfer models)
 * - Spring SecurityContextHolder (for user-based lookups)
 */
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
     * Retrieves a ticket by its unique ID.
     *
     * @param id the ticket ID
     * @return 200 OK with ticket if found, 404 if not
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
     * Creates a new ticket based on request data.
     *
     * @param req the ticket creation request
     * @return 201 Created with the new ticket
     */
    @PostMapping
    public ResponseEntity<TicketDto> createTicket(@RequestBody TicketRequest req) {
        TicketDto dto = ticketService.createTicket(req);
        return ResponseEntity.status(201).body(dto);
    }

    /**
     * Updates an existing ticket by ID.
     *
     * @param id  the ticket ID
     * @param req updated ticket fields
     * @return 200 OK with updated ticket, or 404 if not found
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

    /**
     * Retrieves all tickets associated with a specific buyer's email.
     * Primarily for administrative querying.
     *
     * @param email buyer's email address
     * @return list of tickets purchased by the user
     */
    @GetMapping("/buyer/{email}")
    public List<TicketDto> ticketsForBuyer(@PathVariable String email) {
        return ticketService.findByBuyerEmail(email);
    }

    /**
     * Retrieves all tickets purchased by the currently authenticated user.
     *
     * @return list of tickets belonging to the logged-in user
     */
    @GetMapping("/me")
    public List<TicketDto> myTickets() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ticketService.findByBuyerEmail(email);
    }

    /**
     * Retrieves all tickets in the system.
     * Generally restricted to admin users.
     *
     * @return list of all ticket records
     */
    @GetMapping
    public List<TicketDto> getAllTickets() {
        return ticketService.getAllTickets();
    }

    /**
     * Deletes a ticket by ID.
     *
     * @param id the ticket ID
     * @return 204 No Content if deleted, 404 if not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        try {
            ticketService.deleteTicket(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
