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

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TicketControllerTest {

    @Mock private TicketService ticketService;
    @InjectMocks private TicketController ticketController;

    @Test
    void testGetTicket_Found() {
        TicketDto mockDto = new TicketDto(
                1L, "Rock Night", LocalDate.of(2025, 7, 1),
                "Main Hall", "alice@example.com");

        when(ticketService.getTicketById(1L)).thenReturn(mockDto);

        ResponseEntity<TicketDto> response = ticketController.getTicket(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("alice@example.com", response.getBody().getBuyerEmail());
        verify(ticketService).getTicketById(1L);
    }

    @Test
    void testGetTicket_NotFound() {
        when(ticketService.getTicketById(999L))
                .thenThrow(new ResourceNotFoundException("Ticket with id 999 not found"));

        ResponseEntity<TicketDto> response = ticketController.getTicket(999L);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(ticketService).getTicketById(999L);
    }

    @Test
    void testCreateTicket() {
        TicketRequest req = new TicketRequest(2L, "bob@example.com");
        TicketDto created = new TicketDto(
                10L, "Jazz Fest", LocalDate.of(2025, 8, 20),
                "Downtown Arena", "bob@example.com");

        when(ticketService.createTicket(req)).thenReturn(created);

        ResponseEntity<TicketDto> response = ticketController.createTicket(req);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(10L, response.getBody().getId());
        verify(ticketService).createTicket(req);
    }

    @Test
    void testUpdateTicket_Found() {
        TicketRequest req = new TicketRequest(null, "steve@foo.com");
        TicketDto updated = new TicketDto(
                5L, "Updated Show", LocalDate.of(2025, 9, 1),
                "Big Venue", "steve@foo.com");

        when(ticketService.updateTicket(5L, req)).thenReturn(updated);

        ResponseEntity<TicketDto> response = ticketController.updateTicket(5L, req);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("steve@foo.com", response.getBody().getBuyerEmail());
        verify(ticketService).updateTicket(5L, req);
    }

    @Test
    void testUpdateTicket_NotFound() {
        TicketRequest req = new TicketRequest(null, "nobody@void.com");
        when(ticketService.updateTicket(eq(999L), any(TicketRequest.class)))
                .thenThrow(new ResourceNotFoundException("Ticket with id 999 not found"));

        ResponseEntity<TicketDto> response = ticketController.updateTicket(999L, req);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(ticketService).updateTicket(999L, req);
    }
}
