package com.example.concertManagement_Server.controller;

import com.example.concertManagement_Server.dto.TicketDto;
import com.example.concertManagement_Server.dto.TicketRequest;
import com.example.concertManagement_Server.exception.ResourceNotFoundException;
import com.example.concertManagement_Server.service.TicketService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TicketControllerTest {

    @Mock
    private TicketService ticketService;

    @InjectMocks
    private TicketController ticketController;

    /** Verifies getTicket(id) returns HTTP 200 and the correct DTO when found. */
    @Test
    void testGetTicket_Found() {
        TicketDto mockDto = new TicketDto(1L, 2L, "A1", "VIP", "Alice");
        when(ticketService.getTicketById(1L)).thenReturn(mockDto);

        ResponseEntity<TicketDto> response = ticketController.getTicket(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Alice", response.getBody().getBuyerName());
        verify(ticketService).getTicketById(1L);
    }

    /** Confirms getTicket(id) returns HTTP 404 when not found. */
    @Test
    void testGetTicket_NotFound() {
        when(ticketService.getTicketById(999L))
                .thenThrow(new ResourceNotFoundException("Ticket with id 999 not found"));

        ResponseEntity<TicketDto> response = ticketController.getTicket(999L);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(ticketService).getTicketById(999L);
    }

    /** Verifies createTicket returns HTTP 201 and the created DTO. */
    @Test
    void testCreateTicket() {
        TicketRequest req = new TicketRequest(2L, "B2", "GA", "Bob");
        TicketDto createdDto = new TicketDto(10L, 2L, "B2", "GA", "Bob");
        when(ticketService.createTicket(req)).thenReturn(createdDto);

        ResponseEntity<TicketDto> response = ticketController.createTicket(req);

        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(10L, response.getBody().getId());
        verify(ticketService).createTicket(req);
    }

    /** Verifies updateTicket returns HTTP 200 when found. */
    @Test
    void testUpdateTicket_Found() {
        TicketRequest req = new TicketRequest(null, "C3", "VIP", "Bobby");
        TicketDto updatedDto = new TicketDto(5L, null, "C3", "VIP", "Bobby");
        when(ticketService.updateTicket(5L, req)).thenReturn(updatedDto);

        ResponseEntity<TicketDto> response = ticketController.updateTicket(5L, req);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Bobby", response.getBody().getBuyerName());
        verify(ticketService).updateTicket(5L, req);
    }

    /** Confirms updateTicket returns HTTP 404 when not found. */
    @Test
    void testUpdateTicket_NotFound() {
        TicketRequest req = new TicketRequest(null, "X9", "GA", "Nobody");
        when(ticketService.updateTicket(eq(999L), any(TicketRequest.class)))
                .thenThrow(new ResourceNotFoundException("Ticket with id 999 not found"));

        ResponseEntity<TicketDto> response = ticketController.updateTicket(999L, req);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(ticketService).updateTicket(999L, req);
    }
}
