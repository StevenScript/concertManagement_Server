package com.example.concertManagement_Server.controller;

import com.example.concertManagement_Server.model.Event;
import com.example.concertManagement_Server.model.Ticket;
import com.example.concertManagement_Server.service.EventService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventControllerTest {

    @Mock
    private EventService eventService;

    @InjectMocks
    private EventController eventController;

    // Test retrieving an event that exists
    @Test
    void testGetEvent_Found() {
        Event mockEvent = new Event();
        mockEvent.setId(99L);
        mockEvent.setEventDate(LocalDate.of(2025, 5, 10));

        when(eventService.getEventById(99L)).thenReturn(mockEvent);

        ResponseEntity<Event> response = eventController.getEvent(99L);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(LocalDate.of(2025, 5, 10), response.getBody().getEventDate());

        verify(eventService).getEventById(99L);
    }

    // Test retrieving an event that does not exist
    @Test
    void testGetEvent_NotFound() {
        when(eventService.getEventById(999L)).thenReturn(null);

        ResponseEntity<Event> response = eventController.getEvent(999L);
        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());

        verify(eventService).getEventById(999L);
    }

    // Test retrieving an event that does not exist
    @Test
    void testCreateEvent() {
        Event newEvent = new Event();
        newEvent.setEventDate(LocalDate.of(2025, 6, 1));

        Event savedEvent = new Event();
        savedEvent.setId(10L);
        savedEvent.setEventDate(LocalDate.of(2025, 6, 1));

        when(eventService.createEvent(newEvent)).thenReturn(savedEvent);

        ResponseEntity<Event> response = eventController.createEvent(newEvent);
        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(10L, response.getBody().getId());

        verify(eventService).createEvent(newEvent);
    }

    // Test updating an existing event
    @Test
    void testUpdateEvent_Found() {
        Event updatedData = new Event();
        updatedData.setEventDate(LocalDate.of(2025, 7, 10));

        Event updatedResult = new Event();
        updatedResult.setId(5L);
        updatedResult.setEventDate(LocalDate.of(2025, 7, 10));

        when(eventService.updateEvent(5L, updatedData)).thenReturn(updatedResult);

        ResponseEntity<Event> response = eventController.updateEvent(5L, updatedData);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(LocalDate.of(2025, 7, 10), response.getBody().getEventDate());

        verify(eventService).updateEvent(5L, updatedData);
    }

    // Test updating a non-existent event
    @Test
    void testUpdateEvent_NotFound() {
        Event updatedData = new Event();
        updatedData.setEventDate(LocalDate.of(2025, 10, 1));

        when(eventService.updateEvent(999L, updatedData)).thenReturn(null);

        ResponseEntity<Event> response = eventController.updateEvent(999L, updatedData);
        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());

        verify(eventService).updateEvent(999L, updatedData);
    }

    // Test adding an artist to an existing event
    @Test
    void testAddArtistToEvent_Found() {
        Long artistId = 101L;  // ✅ Use artist ID instead of an object
        Event updatedEvent = new Event();
        updatedEvent.setId(5L);

        when(eventService.addArtistToEvent(5L, artistId)).thenReturn(updatedEvent);

        ResponseEntity<Event> response = eventController.addArtistToEvent(5L, artistId);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        verify(eventService).addArtistToEvent(5L, artistId);
    }

    // Test adding an artist to a non-existent event
    @Test
    void testAddArtistToEvent_NotFound() {
        Long artistId = 999L;

        when(eventService.addArtistToEvent(999L, artistId)).thenReturn(null);

        ResponseEntity<Event> response = eventController.addArtistToEvent(999L, artistId);
        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(eventService).addArtistToEvent(999L, artistId);
    }

    // Test listing all events for an artist
    @Test
    void testListAllEventsForArtist() {
        Event mockEvent = new Event();
        mockEvent.setId(1L);
        mockEvent.setEventDate(LocalDate.of(2025, 5, 10));

        when(eventService.listAllEventsForArtist(10L))
                .thenReturn(Collections.singletonList(mockEvent));

        ResponseEntity<List<Event>> response = eventController.getEventsByArtistId(10L);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals(LocalDate.of(2025, 5, 10), response.getBody().get(0).getEventDate());

        verify(eventService).listAllEventsForArtist(10L);
    }

    @Test
    void testGetTicketsForEvent() {
        // Define a sample event ID for testing
        Long eventId = 2L;

        // Create a sample ticket object
        Ticket ticket = new Ticket();
        ticket.setId(501L);

        // Prepare the expected list of tickets
        List<Ticket> tickets = Collections.singletonList(ticket);

        // Mock the service layer response.
        when(eventService.getTicketsForEvent(eventId)).thenReturn(tickets);

        // Call the method in the controller
        ResponseEntity<List<Ticket>> response = eventController.getTicketsForEvent(eventId);

        // Assertions:
        assertEquals(200, response.getStatusCodeValue(), "Expected HTTP status 200 OK.");
        assertNotNull(response.getBody(), "Response body should not be null.");
        assertEquals(1, response.getBody().size(), "Expected one ticket in the list.");
        assertEquals(501L, response.getBody().get(0).getId(), "Ticket ID should match the test ticket.");

        // Verify that the service method was invoked with the correct eventId
        verify(eventService).getTicketsForEvent(eventId);
    }

    @Test
    void testGetTicketCountForEvent() {
        Long eventId = 2L;
        // Assume the total number of tickets for the event is 150
        when(eventService.getTicketCountForEvent(eventId)).thenReturn(150L);

        // Call the new endpoint method to get ticket count
        ResponseEntity<Long> response = eventController.getTicketCountForEvent(eventId);

        // Assertions:
        assertEquals(200, response.getStatusCodeValue(), "Expected HTTP status 200 OK.");
        assertEquals(150L, response.getBody(), "Ticket count should match the expected value.");

        // Verify that the service method was invoked correctly
        verify(eventService).getTicketCountForEvent(eventId);
    }


}

