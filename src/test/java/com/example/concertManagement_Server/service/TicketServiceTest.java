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
}
