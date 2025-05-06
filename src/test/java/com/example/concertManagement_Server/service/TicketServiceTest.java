package com.example.concertManagement_Server.service;

import com.example.concertManagement_Server.dto.TicketDto;
import com.example.concertManagement_Server.dto.TicketRequest;
import com.example.concertManagement_Server.exception.ResourceNotFoundException;
import com.example.concertManagement_Server.mapper.TicketMapper;
import com.example.concertManagement_Server.model.Event;
import com.example.concertManagement_Server.model.Ticket;
import com.example.concertManagement_Server.repository.EventRepository;
import com.example.concertManagement_Server.repository.TicketRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {

    @Mock private TicketRepository ticketRepository;
    @Mock private EventRepository  eventRepository;
    @Mock private TicketMapper     ticketMapper;  // new

    @InjectMocks private TicketService ticketService;

    @Test
    void testGetTicketById_Found() {
        Ticket entity = new Ticket();
        entity.setId(1L);
        Event event = new Event();
        event.setName("Show"); event.setEventDate(LocalDate.of(2025,5,1));
        entity.setEvent(event);
        entity.setBuyerEmail("john@doe.com");

        TicketDto dto = new TicketDto(1L,"Show",
                LocalDate.of(2025,5,1), null,"john@doe.com");

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(ticketMapper.toDto(entity)).thenReturn(dto);

        TicketDto result = ticketService.getTicketById(1L);
        assertEquals(1L, result.getId());
        assertEquals("john@doe.com", result.getBuyerEmail());

        verify(ticketRepository).findById(1L);
        verify(ticketMapper).toDto(entity);
    }

    @Test
    void testGetTicketById_NotFound() {
        when(ticketRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> ticketService.getTicketById(999L));
    }

    @Test
    void testCreateTicket() {
        TicketRequest req = new TicketRequest(3L, "mark@foo.com");
        Event event = new Event(); event.setId(3L); event.setName("Concert");
        event.setEventDate(LocalDate.of(2025,8,1));

        Ticket saved = new Ticket();
        saved.setId(200L);
        saved.setEvent(event);
        saved.setBuyerEmail("mark@foo.com");

        TicketDto dto = new TicketDto(200L,"Concert",
                LocalDate.of(2025,8,1), null,"mark@foo.com");

        when(eventRepository.findById(3L)).thenReturn(Optional.of(event));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(saved);
        when(ticketMapper.toDto(saved)).thenReturn(dto);

        TicketDto result = ticketService.createTicket(req);
        assertEquals(200L, result.getId());

        verify(eventRepository).findById(3L);
        verify(ticketRepository).save(any(Ticket.class));
        verify(ticketMapper).toDto(saved);
    }

    @Test
    void testUpdateTicket_NotFound() {
        when(ticketRepository.findById(999L)).thenReturn(Optional.empty());
        TicketRequest req = new TicketRequest(null, "nobody@void.com");

        assertThrows(ResourceNotFoundException.class,
                () -> ticketService.updateTicket(999L, req));

        verify(ticketRepository).findById(999L);
        verify(ticketRepository, never()).save(any());
    }
}


