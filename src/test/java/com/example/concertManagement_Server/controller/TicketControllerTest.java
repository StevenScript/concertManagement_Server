package com.example.concertManagement_Server.controller;

import com.example.concertManagement_Server.model.Ticket;
import com.example.concertManagement_Server.service.TicketService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
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

    @Test
    void testGetTicket_Found() {
        Ticket mockTicket = new Ticket();
        mockTicket.setId(1L);
        mockTicket.setBuyerName("Alice");

        when(ticketService.getTicketById(1L)).thenReturn(mockTicket);

        ResponseEntity<Ticket> response = ticketController.getTicket(1L);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Alice", response.getBody().getBuyerName());
        verify(ticketService).getTicketById(1L);
    }

    @Test
    void testGetTicket_NotFound() {
        when(ticketService.getTicketById(999L)).thenReturn(null);

        ResponseEntity<Ticket> response = ticketController.getTicket(999L);
        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(ticketService).getTicketById(999L);
    }

    @Test
    void testCreateTicket() {
        Ticket newTicket = new Ticket();
        newTicket.setBuyerName("Bob");

        Ticket savedTicket = new Ticket();
        savedTicket.setId(10L);
        savedTicket.setBuyerName("Bob");

        when(ticketService.createTicket(newTicket)).thenReturn(savedTicket);

        ResponseEntity<Ticket> response = ticketController.createTicket(newTicket);
        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(10L, response.getBody().getId());

        verify(ticketService).createTicket(newTicket);
    }

    @Test
    void testUpdateTicket_Found() {
        Ticket updatedData = new Ticket();
        updatedData.setBuyerName("Bobby");

        Ticket updatedTicket = new Ticket();
        updatedTicket.setId(5L);
        updatedTicket.setBuyerName("Bobby");

        when(ticketService.updateTicket(5L, updatedData)).thenReturn(updatedTicket);

        ResponseEntity<Ticket> response = ticketController.updateTicket(5L, updatedData);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Bobby", response.getBody().getBuyerName());
        verify(ticketService).updateTicket(5L, updatedData);
    }

    @Test
    void testUpdateTicket_NotFound() {
        Ticket updatedData = new Ticket();
        updatedData.setBuyerName("NoOne");

        when(ticketService.updateTicket(999L, updatedData)).thenReturn(null);

        ResponseEntity<Ticket> response = ticketController.updateTicket(999L, updatedData);
        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(ticketService).updateTicket(999L, updatedData);
    }
}
