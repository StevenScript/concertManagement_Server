package com.example.concertManagement_Server.service;

import com.example.concertManagement_Server.model.Artist;
import com.example.concertManagement_Server.model.Event;
import com.example.concertManagement_Server.repository.EventRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventService eventService;

    @Test
    void testGetEventById_Found() {
        Event mockEvent = new Event();
        mockEvent.setId(50L);
        mockEvent.setEventDate(LocalDate.of(2025, 5, 10));

        when(eventRepository.findById(50L)).thenReturn(Optional.of(mockEvent));

        Event result = eventService.getEventById(50L);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(LocalDate.of(2025, 5, 10), result.getEventDate());
        verify(eventRepository).findById(50L);
    }

    @Test
    void testGetEventById_NotFound() {
        when(eventRepository.findById(999L)).thenReturn(Optional.empty());

        Event result = eventService.getEventById(999L);
        Assertions.assertNull(result);
        verify(eventRepository).findById(999L);
    }

    @Test
    void testCreateEvent() {
        Event newEvent = new Event();
        newEvent.setEventDate(LocalDate.of(2025, 8, 20));
        newEvent.setTicketPrice(99.99);

        Event savedEvent = new Event();
        savedEvent.setId(101L);
        savedEvent.setEventDate(LocalDate.of(2025, 8, 20));
        savedEvent.setTicketPrice(99.99);

        when(eventRepository.save(newEvent)).thenReturn(savedEvent);

        Event result = eventService.createEvent(newEvent);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(101L, result.getId());
        Assertions.assertEquals(LocalDate.of(2025, 8, 20), result.getEventDate());
        verify(eventRepository).save(newEvent);
    }

    @Test
    void testUpdateEvent_Found() {
        Event existing = new Event();
        existing.setId(5L);
        existing.setEventDate(LocalDate.of(2025, 5, 10));
        existing.setTicketPrice(50.0);

        when(eventRepository.findById(5L)).thenReturn(Optional.of(existing));
        when(eventRepository.save(any(Event.class))).thenAnswer(inv -> inv.getArgument(0));

        Event updatedData = new Event();
        updatedData.setEventDate(LocalDate.of(2025, 6, 15));
        updatedData.setTicketPrice(60.0);

        Event result = eventService.updateEvent(5L, updatedData);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(LocalDate.of(2025, 6, 15), result.getEventDate());
        Assertions.assertEquals(60.0, result.getTicketPrice());
        verify(eventRepository).findById(5L);
        verify(eventRepository).save(any(Event.class));
    }

    @Test
    void testUpdateEvent_NotFound() {
        when(eventRepository.findById(999L)).thenReturn(Optional.empty());

        Event updatedData = new Event();
        updatedData.setEventDate(LocalDate.of(2025, 1, 1));

        Event result = eventService.updateEvent(999L, updatedData);
        Assertions.assertNull(result);
        verify(eventRepository).findById(999L);
        verify(eventRepository, never()).save(any(Event.class));
    }

    @Test
    void testAddArtistToEvent() {
        // Assume an event with ID=5
        Event existing = new Event();
        existing.setId(5L);

        // The event's "artists" Set is initially empty or has some members
        // Attempt to add a new Artist

        when(eventRepository.findById(5L)).thenReturn(Optional.of(existing));
        when(eventRepository.save(any(Event.class))).thenAnswer(inv -> inv.getArgument(0));

        Artist newArtist = new Artist();
        newArtist.setId(100L);
        newArtist.setStageName("New Artist");

        // Act
        Event result = eventService.addArtistToEvent(5L, newArtist);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.getArtists().size());
        Assertions.assertTrue(result.getArtists().contains(newArtist));

        verify(eventRepository).findById(5L);
        verify(eventRepository).save(any(Event.class));
    }

    @Test
    void testListAllEventsForArtist() {
        // Repository returns one event
        Event mockEvent = new Event();
        mockEvent.setId(1L);
        mockEvent.setEventDate(LocalDate.of(2025, 5, 10));

        when(eventRepository.findEventsByArtistId(10L))
                .thenReturn(Collections.singletonList(mockEvent));

        List<Event> results = eventService.listAllEventsForArtist(10L);
        assertEquals(1, results.size());
        assertEquals(LocalDate.of(2025, 5, 10), results.get(0).getEventDate());

        verify(eventRepository).findEventsByArtistId(10L);
    }
}
