package com.example.concertManagement_Server.service;

import com.example.concertManagement_Server.model.Ticket;
import com.example.concertManagement_Server.repository.TicketRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private TicketService ticketService;

    @Test
    void testGetTicketById_Found() {
        Ticket mockTicket = new Ticket();
        mockTicket.setId(1L);
        mockTicket.setBuyerName("John Doe");

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(mockTicket));

        Ticket result = ticketService.getTicketById(1L);
        Assertions.assertNotNull(result);
        Assertions.assertEquals("John Doe", result.getBuyerName());
        verify(ticketRepository).findById(1L);
    }

    @Test
    void testGetTicketById_NotFound() {
        when(ticketRepository.findById(999L)).thenReturn(Optional.empty());

        Ticket result = ticketService.getTicketById(999L);
        Assertions.assertNull(result);
        verify(ticketRepository).findById(999L);
    }

    @Test
    void testCreateTicket() {
        Ticket newTicket = new Ticket();
        newTicket.setBuyerName("Mark");
        newTicket.setTicketType("VIP");

        Ticket savedTicket = new Ticket();
        savedTicket.setId(200L);
        savedTicket.setBuyerName("Mark");
        savedTicket.setTicketType("VIP");

        when(ticketRepository.save(newTicket)).thenReturn(savedTicket);

        Ticket result = ticketService.createTicket(newTicket);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(200L, result.getId());
        Assertions.assertEquals("VIP", result.getTicketType());
        verify(ticketRepository).save(newTicket);
    }

    @Test
    void testUpdateTicket_Found() {
        Ticket existing = new Ticket();
        existing.setId(5L);
        existing.setBuyerName("Steve");
        existing.setTicketType("GA");

        when(ticketRepository.findById(5L)).thenReturn(Optional.of(existing));
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(inv -> inv.getArgument(0));

        Ticket updatedData = new Ticket();
        updatedData.setBuyerName("Steven");
        updatedData.setTicketType("VIP");

        Ticket result = ticketService.updateTicket(5L, updatedData);
        Assertions.assertNotNull(result);
        Assertions.assertEquals("Steven", result.getBuyerName());
        Assertions.assertEquals("VIP", result.getTicketType());

        verify(ticketRepository).findById(5L);
        verify(ticketRepository).save(any(Ticket.class));
    }

    @Test
    void testUpdateTicket_NotFound() {
        when(ticketRepository.findById(999L)).thenReturn(Optional.empty());

        Ticket updatedData = new Ticket();
        updatedData.setBuyerName("Not Found");

        Ticket result = ticketService.updateTicket(999L, updatedData);
        Assertions.assertNull(result);
        verify(ticketRepository).findById(999L);
        verify(ticketRepository, never()).save(any(Ticket.class));
    }


}
