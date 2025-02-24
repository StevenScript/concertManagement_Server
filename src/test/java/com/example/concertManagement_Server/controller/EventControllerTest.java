package com.example.concertManagement_Server.controller;

import com.example.concertManagement_Server.model.Artist;
import com.example.concertManagement_Server.model.Event;
import com.example.concertManagement_Server.service.EventService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventControllerTest {

    @Mock
    private EventService eventService; // Mock the service

    @InjectMocks
    private EventController eventController;

    @Test
    void testGetEvent_Found() {
        // Suppose the service returns an event for ID=99
        Event mockEvent = new Event();
        mockEvent.setId(99L);
        mockEvent.setEventDate(LocalDate.of(2025, 5, 10));

        when(eventService.getEventById(99L)).thenReturn(mockEvent);

        ResponseEntity<Event> response = eventController.getEvent(99L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(LocalDate.of(2025, 5, 10), response.getBody().getEventDate());

        verify(eventService).getEventById(99L);
    }

    @Test
    void testGetEvent_NotFound() {
        when(eventService.getEventById(999L)).thenReturn(null);

        ResponseEntity<Event> response = eventController.getEvent(999L);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());

        verify(eventService).getEventById(999L);
    }
}
