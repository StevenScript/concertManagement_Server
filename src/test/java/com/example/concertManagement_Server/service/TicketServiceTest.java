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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit-tests for {@link TicketService}.
 */
@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock private TicketRepository ticketRepository;
    @Mock private EventRepository  eventRepository;
    @Mock private TicketMapper     ticketMapper;

    @InjectMocks private TicketService ticketService;

    /* ---------- GET ---------- */

    @Test
    void getTicket_found_returnsDto() {
        // given entity
        Event ev = new Event();
        ev.setName("Show"); ev.setEventDate(LocalDate.of(2025,5,1));

        Ticket entity = new Ticket();
        entity.setId(1L); entity.setEvent(ev);
        entity.setBuyerEmail("john@doe.com");

        // mapped dto
        TicketDto dto = new TicketDto(
                1L, "Show", ev.getEventDate(), null, "john@doe.com");

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(ticketMapper.toDto(entity)).thenReturn(dto);

        // when
        TicketDto result = ticketService.getTicketById(1L);

        // then
        assertEquals(dto, result);
        verify(ticketRepository).findById(1L);
        verify(ticketMapper).toDto(entity);
    }

    @Test
    void getTicket_notFound_throws404() {
        when(ticketRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> ticketService.getTicketById(999L));
    }

    /* ---------- CREATE ---------- */

    @Test
    void createTicket_success_returnsDto() {
        // request
        TicketRequest req = new TicketRequest(3L, "mark@foo.com");

        // event
        Event ev = new Event();
        ev.setId(3L); ev.setName("Concert");
        ev.setEventDate(LocalDate.of(2025,8,1));
        ev.setAvailableTickets(100);

        // entity to persist
        Ticket saved = new Ticket();
        saved.setId(200L); saved.setEvent(ev); saved.setBuyerEmail("mark@foo.com");

        // mapped dto
        TicketDto dto = new TicketDto(
                200L, "Concert", ev.getEventDate(), null, "mark@foo.com");

        when(eventRepository.findById(3L)).thenReturn(Optional.of(ev));
        when(ticketRepository.countByEventId(3L)).thenReturn(0L);  // capacity OK
        when(ticketRepository.save(any(Ticket.class))).thenReturn(saved);
        when(ticketMapper.toDto(saved)).thenReturn(dto);

        // when / then
        TicketDto result = ticketService.createTicket(req);
        assertEquals(dto, result);

        verify(eventRepository).findById(3L);
        verify(ticketRepository).save(any(Ticket.class));
        verify(ticketMapper).toDto(saved);
    }

    @Test
    void createTicket_capacityExceeded_throws400() {
        Event ev = new Event();
        ev.setId(1L); ev.setAvailableTickets(1);            // capacity = 1

        when(eventRepository.findById(1L)).thenReturn(Optional.of(ev));
        when(ticketRepository.countByEventId(1L)).thenReturn(1L); // already sold 1

        TicketRequest req = new TicketRequest(1L, "buyer@foo.com");

        assertThrows(CapacityExceededException.class,
                () -> ticketService.createTicket(req));
    }

    /* ---------- UPDATE ---------- */

    @Test
    void updateTicket_notFound_throws404() {
        when(ticketRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> ticketService.updateTicket(999L,
                        new TicketRequest(null, "x@y.com")));
        verify(ticketRepository).findById(999L);
        verify(ticketRepository, never()).save(any());
    }
}



