package com.example.concertManagement_Server.service;

import com.example.concertManagement_Server.model.Event;
import com.example.concertManagement_Server.repository.EventRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

    @Mock
    private EventRepository eventRepository; // no real DB

    @InjectMocks
    private EventService eventService; // DOES NOT EXIST YET

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
}
