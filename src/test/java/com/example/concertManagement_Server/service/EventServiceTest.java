package com.example.concertManagement_Server.service;

import com.example.concertManagement_Server.exception.ResourceNotFoundException;
import com.example.concertManagement_Server.model.Artist;
import com.example.concertManagement_Server.model.Event;
import com.example.concertManagement_Server.model.Ticket;
import com.example.concertManagement_Server.repository.ArtistRepository;
import com.example.concertManagement_Server.repository.EventRepository;
import com.example.concertManagement_Server.repository.TicketRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

    @Mock private EventRepository eventRepository;
    @Mock private ArtistRepository artistRepository;
    @Mock private TicketRepository ticketRepository;

    @InjectMocks private EventService eventService;

    @Test
    void testGetAllEvents() {
        Event e1 = new Event(); e1.setId(1L); e1.setName("A");
        Event e2 = new Event(); e2.setId(2L); e2.setName("B");
        when(eventRepository.findAll()).thenReturn(List.of(e1, e2));

        List<Event> results = eventService.getAllEvents();
        assertEquals(2, results.size());
        assertEquals(1L, results.get(0).getId());
        assertEquals(2L, results.get(1).getId());
        verify(eventRepository).findAll();
    }

    @Test
    void testFindUpcomingEvents() {
        LocalDate today = LocalDate.now();
        Event upcoming = new Event();
        upcoming.setId(3L);
        upcoming.setName("Future");
        upcoming.setEventDate(today.plusDays(1));

        when(eventRepository.findByEventDateAfterOrderByEventDateAsc(today))
                .thenReturn(List.of(upcoming));

        List<Event> results = eventService.findUpcomingEvents();
        assertEquals(1, results.size());
        assertEquals(3L, results.get(0).getId());
        verify(eventRepository).findByEventDateAfterOrderByEventDateAsc(today);
    }

    @Test
    void testGetEventById_Found() {
        Event mockEvent = new Event();
        mockEvent.setId(50L);
        mockEvent.setName("Found");
        mockEvent.setEventDate(LocalDate.of(2025, 5, 10));

        when(eventRepository.findById(50L)).thenReturn(Optional.of(mockEvent));

        Event result = eventService.getEventById(50L);
        assertNotNull(result);
        assertEquals(LocalDate.of(2025, 5, 10), result.getEventDate());
        verify(eventRepository).findById(50L);
    }

    @Test
    void testGetEventById_NotFound() {
        when(eventRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> eventService.getEventById(999L));
        verify(eventRepository).findById(999L);
    }

    @Test
    void testCreateEvent() {
        Event newEvent = new Event();
        newEvent.setName("Create");
        newEvent.setEventDate(LocalDate.of(2025, 8, 20));
        newEvent.setTicketPrice(99.99);

        Event savedEvent = new Event();
        savedEvent.setId(101L);
        savedEvent.setName("Create");
        savedEvent.setEventDate(LocalDate.of(2025, 8, 20));
        savedEvent.setTicketPrice(99.99);

        when(eventRepository.save(newEvent)).thenReturn(savedEvent);

        Event result = eventService.createEvent(newEvent);
        assertNotNull(result);
        assertEquals(101L, result.getId());
        assertEquals(LocalDate.of(2025, 8, 20), result.getEventDate());
        verify(eventRepository).save(newEvent);
    }

    @Test
    void testUpdateEvent_Found() {
        Event existing = new Event();
        existing.setId(5L);
        existing.setName("Old");
        existing.setEventDate(LocalDate.of(2025, 5, 10));
        existing.setTicketPrice(50.0);

        when(eventRepository.findById(5L)).thenReturn(Optional.of(existing));
        when(eventRepository.save(any(Event.class))).thenAnswer(inv -> inv.getArgument(0));

        Event updatedData = new Event();
        updatedData.setName("Old");
        updatedData.setEventDate(LocalDate.of(2025, 6, 15));
        updatedData.setTicketPrice(60.0);

        Event result = eventService.updateEvent(5L, updatedData);
        assertNotNull(result);
        assertEquals(LocalDate.of(2025, 6, 15), result.getEventDate());
        assertEquals(60.0, result.getTicketPrice());
        verify(eventRepository).findById(5L);
        verify(eventRepository).save(any(Event.class));
    }

    @Test
    void testUpdateEvent_NotFound() {
        when(eventRepository.findById(999L)).thenReturn(Optional.empty());

        Event updatedData = new Event();
        updatedData.setName("X");
        updatedData.setEventDate(LocalDate.of(2025, 1, 1));

        assertThrows(ResourceNotFoundException.class,
                () -> eventService.updateEvent(999L, updatedData));
        verify(eventRepository).findById(999L);
        verify(eventRepository, never()).save(any(Event.class));
    }

    @Test
    void testAddArtistToEvent_Success() {
        Event existingEvent = new Event();
        existingEvent.setId(5L);
        existingEvent.setName("AddArtist");
        existingEvent.setArtists(new HashSet<>());

        Artist newArtist = new Artist();
        newArtist.setId(100L);
        newArtist.setStageName("New Artist");

        when(eventRepository.findById(5L)).thenReturn(Optional.of(existingEvent));
        when(artistRepository.findById(100L)).thenReturn(Optional.of(newArtist));
        when(eventRepository.save(any(Event.class))).thenAnswer(inv -> inv.getArgument(0));

        Event result = eventService.addArtistToEvent(5L, 100L);

        assertNotNull(result);
        assertEquals(1, result.getArtists().size());
        assertTrue(result.getArtists().stream().anyMatch(a -> a.getId().equals(100L)));
        verify(eventRepository).findById(5L);
        verify(artistRepository).findById(100L);
        verify(eventRepository).save(any(Event.class));
    }

    @Test
    void testAddArtistToEvent_EventNotFound() {
        when(eventRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> eventService.addArtistToEvent(999L, 100L));
        verify(eventRepository).findById(999L);
        verify(artistRepository, never()).findById(anyLong());
        verify(eventRepository, never()).save(any(Event.class));
    }

    @Test
    void testAddArtistToEvent_ArtistNotFound() {
        Event existingEvent = new Event();
        existingEvent.setId(5L);
        existingEvent.setName("NoArtist");
        existingEvent.setArtists(new HashSet<>());

        when(eventRepository.findById(5L)).thenReturn(Optional.of(existingEvent));
        when(artistRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> eventService.addArtistToEvent(5L, 999L));
        verify(eventRepository).findById(5L);
        verify(artistRepository).findById(999L);
        verify(eventRepository, never()).save(any(Event.class));
    }

    @Test
    void testListAllEventsForArtist() {
        Event mockEvent = new Event();
        mockEvent.setId(1L);
        mockEvent.setName("ArtistEvent");
        mockEvent.setEventDate(LocalDate.of(2025, 5, 10));

        when(eventRepository.findEventsByArtistId(10L))
                .thenReturn(Collections.singletonList(mockEvent));

        List<Event> results = eventService.listAllEventsForArtist(10L);
        assertEquals(1, results.size());
        assertEquals(LocalDate.of(2025, 5, 10), results.get(0).getEventDate());
        verify(eventRepository).findEventsByArtistId(10L);
    }

    @Test
    void testGetTicketsForEvent() {
        Ticket t = new Ticket();
        t.setId(501L);
        when(ticketRepository.findByEventId(2L)).thenReturn(List.of(t));

        List<Ticket> tickets = eventService.getTicketsForEvent(2L);
        assertEquals(1, tickets.size());
        assertEquals(501L, tickets.get(0).getId());
        verify(ticketRepository).findByEventId(2L);
    }

    @Test
    void testGetTicketCountForEvent() {
        when(ticketRepository.countByEventId(2L)).thenReturn(150L);

        Long count = eventService.getTicketCountForEvent(2L);
        assertEquals(150L, count);
        verify(ticketRepository).countByEventId(2L);
    }
}

