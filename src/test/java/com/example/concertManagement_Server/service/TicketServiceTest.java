package com.example.concertManagement_Server.service;

import com.example.concertManagement_Server.dto.TicketDto;
import com.example.concertManagement_Server.dto.TicketRequest;
import com.example.concertManagement_Server.exception.ResourceNotFoundException;
import com.example.concertManagement_Server.model.Event;
import com.example.concertManagement_Server.model.Ticket;
import com.example.concertManagement_Server.repository.EventRepository;
import com.example.concertManagement_Server.repository.TicketRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private TicketService ticketService;

    @Test
    void testGetTicketById_Found() {
        Ticket entity = new Ticket();
        entity.setId(1L);
        Event event = new Event(); event.setId(2L);
        entity.setEvent(event);
        entity.setBuyerName("John Doe");

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(entity));

        TicketDto dto = ticketService.getTicketById(1L);
        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals(2L, dto.getEventId());
        assertEquals("John Doe", dto.getBuyerName());
        verify(ticketRepository).findById(1L);
    }

    @Test
    void testGetTicketById_NotFound() {
        when(ticketRepository.findById(999L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> ticketService.getTicketById(999L)
        );
        assertEquals("Ticket with id 999 not found", ex.getMessage());
        verify(ticketRepository).findById(999L);
    }

    @Test
    void testCreateTicket() {
        TicketRequest req = new TicketRequest(3L, "C3", "VIP", "Mark");
        Event event = new Event(); event.setId(3L);
        Ticket toSave = new Ticket();
        toSave.setEvent(event);
        toSave.setSeatNumber(req.getSeatNumber());
        toSave.setTicketType(req.getTicketType());
        toSave.setBuyerName(req.getBuyerName());

        Ticket saved = new Ticket();
        saved.setId(200L);
        saved.setEvent(event);
        saved.setSeatNumber("C3");
        saved.setTicketType("VIP");
        saved.setBuyerName("Mark");

        when(eventRepository.findById(3L)).thenReturn(Optional.of(event));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(saved);

        TicketDto dto = ticketService.createTicket(req);
        assertNotNull(dto);
        assertEquals(200L, dto.getId());
        assertEquals("C3", dto.getSeatNumber());
        assertEquals("Mark", dto.getBuyerName());

        verify(eventRepository).findById(3L);
        verify(ticketRepository).save(any(Ticket.class));
    }

    @Test
    void testUpdateTicket_Found() {
        Event event = new Event(); event.setId(4L);
        Ticket existing = new Ticket(); existing.setId(5L); existing.setEvent(event);
        existing.setBuyerName("Steve"); existing.setTicketType("GA"); existing.setSeatNumber("D5");

        TicketRequest req = new TicketRequest(null, "E6", "VIP", "Steven");
        when(ticketRepository.findById(5L)).thenReturn(Optional.of(existing));
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(inv -> inv.getArgument(0));

        TicketDto dto = ticketService.updateTicket(5L, req);
        assertEquals(5L, dto.getId());
        assertEquals("E6", dto.getSeatNumber());
        assertEquals("VIP", dto.getTicketType());
        assertEquals("Steven", dto.getBuyerName());

        verify(ticketRepository).findById(5L);
        verify(ticketRepository).save(any(Ticket.class));
    }

    @Test
    void testUpdateTicket_NotFound() {
        when(ticketRepository.findById(999L)).thenReturn(Optional.empty());
        TicketRequest req = new TicketRequest(null, "F7", "GA", "Nobody");

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> ticketService.updateTicket(999L, req)
        );
        assertEquals("Ticket with id 999 not found", ex.getMessage());
        verify(ticketRepository).findById(999L);
        verify(ticketRepository, never()).save(any());
    }
}


