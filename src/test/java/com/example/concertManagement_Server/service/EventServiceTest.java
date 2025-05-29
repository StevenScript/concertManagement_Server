package com.example.concertManagement_Server.service;

import com.example.concertManagement_Server.exception.ResourceNotFoundException;
import com.example.concertManagement_Server.model.Artist;
import com.example.concertManagement_Server.model.Event;
import com.example.concertManagement_Server.model.Ticket;
import com.example.concertManagement_Server.model.Venue;
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

/**
 * Unit-tests for {@link EventService}.
 * Only edits compared with the old file:
 *   – new / update tests now attach a Venue with capacity to avoid NPE.
 */
@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    /* ---------- mocks ---------- */
    @Mock private EventRepository  eventRepository;
    @Mock private ArtistRepository artistRepository;
    @Mock private TicketRepository ticketRepository;

    @InjectMocks private EventService eventService;

    /* ------------------------------------------------------------ */

    @Test
    void testGetAllEvents() {
        Event e1 = new Event(); e1.setId(1L); e1.setName("A");
        Event e2 = new Event(); e2.setId(2L); e2.setName("B");
        when(eventRepository.findAll()).thenReturn(List.of(e1, e2));

        List<Event> results = eventService.getAllEvents();
        assertEquals(2, results.size());
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
        verify(eventRepository)
                .findByEventDateAfterOrderByEventDateAsc(today);
    }

    @Test
    void testGetEventById_Found() {
        Event ev = new Event();
        ev.setId(50L);
        ev.setName("Found");
        ev.setEventDate(LocalDate.of(2025, 5, 10));

        when(eventRepository.findById(50L)).thenReturn(Optional.of(ev));

        Event result = eventService.getEventById(50L);
        assertEquals(LocalDate.of(2025, 5, 10), result.getEventDate());
    }

    @Test
    void testGetEventById_NotFound() {
        when(eventRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> eventService.getEventById(999L));
    }

    /* ---------- CREATE (venue capacity needed) ---------- */

    @Test
    void testCreateEvent() {
        Venue v = new Venue();
        v.setCapacity(500);

        Event newEv = new Event();
        newEv.setName("Create");
        newEv.setEventDate(LocalDate.of(2025, 8, 20));
        newEv.setTicketPrice(99.99);
        newEv.setAvailableTickets(200);
        newEv.setVenue(v);                         // <- non-null venue

        Event saved = new Event();
        saved.setId(101L);
        saved.setName("Create");
        saved.setEventDate(newEv.getEventDate());
        saved.setTicketPrice(newEv.getTicketPrice());
        saved.setAvailableTickets(200);
        saved.setVenue(v);

        when(eventRepository.save(newEv)).thenReturn(saved);

        Event result = eventService.createEvent(newEv);
        assertEquals(101L, result.getId());
        verify(eventRepository).save(newEv);
    }

    /* ---------- UPDATE ---------- */

    @Test
    void testUpdateEvent_Found() {
        Venue v = new Venue(); v.setCapacity(500);

        Event existing = new Event();
        existing.setId(5L);
        existing.setName("Old");
        existing.setEventDate(LocalDate.of(2025, 5, 10));
        existing.setTicketPrice(50.0);
        existing.setAvailableTickets(100);
        existing.setVenue(v);

        when(eventRepository.findById(5L)).thenReturn(Optional.of(existing));
        when(eventRepository.save(any(Event.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        Event patch = new Event();
        patch.setName("Old");                          // unchanged
        patch.setEventDate(LocalDate.of(2025, 6, 15)); // new date
        patch.setTicketPrice(60.0);
        patch.setAvailableTickets(120);
        patch.setVenue(v);

        Event result = eventService.updateEvent(5L, patch);

        assertEquals(LocalDate.of(2025, 6, 15), result.getEventDate());
        assertEquals(60.0, result.getTicketPrice());
        verify(eventRepository).findById(5L);
        verify(eventRepository).save(any(Event.class));
    }

    @Test
    void testUpdateEvent_NotFound() {
        when(eventRepository.findById(999L)).thenReturn(Optional.empty());

        Event patch = new Event();
        patch.setName("X");
        patch.setEventDate(LocalDate.of(2025, 1, 1));

        assertThrows(ResourceNotFoundException.class,
                () -> eventService.updateEvent(999L, patch));
    }

    /* ---------- ADD-ARTIST ---------- */

    @Test
    void testAddArtistToEvent_Success() {
        Event ev = new Event(); ev.setId(5L); ev.setArtists(new HashSet<>());
        Artist art = new Artist(); art.setId(100L);

        when(eventRepository.findById(5L)).thenReturn(Optional.of(ev));
        when(artistRepository.findById(100L)).thenReturn(Optional.of(art));
        when(eventRepository.save(any(Event.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        Event result = eventService.addArtistToEvent(5L, 100L);

        assertTrue(result.getArtists()
                .stream()
                .anyMatch(a -> a.getId().equals(100L)));
    }

    /* ---------- OTHER READS ---------- */

    @Test
    void testListAllEventsForArtist() {
        Event ev = new Event(); ev.setId(1L);
        when(eventRepository.findEventsByArtistId(10L))
                .thenReturn(List.of(ev));

        List<Event> results = eventService.listAllEventsForArtist(10L);
        assertEquals(1, results.size());
    }

    @Test
    void testGetTicketsForEvent_Count() {
        Ticket t = new Ticket(); t.setId(501L);
        when(ticketRepository.findByEventId(2L)).thenReturn(List.of(t));
        when(ticketRepository.countByEventId(2L)).thenReturn(150L);

        assertEquals(1, eventService.getTicketsForEvent(2L).size());
        assertEquals(150L, eventService.getTicketCountForEvent(2L));
    }
}
