package com.example.concertManagement_Server.service;

import com.example.concertManagement_Server.dto.VenueRequest;
import com.example.concertManagement_Server.exception.ResourceNotFoundException;
import com.example.concertManagement_Server.mapper.VenueMapper;
import com.example.concertManagement_Server.model.Venue;
import com.example.concertManagement_Server.repository.EventRepository;
import com.example.concertManagement_Server.repository.VenueRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class VenueServiceTest {

    @Mock
    private VenueRepository venueRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private VenueMapper venueMapper;

    @InjectMocks
    private VenueService venueService;

    @Test
    void testGetVenueById_Found() {
        Venue v = new Venue();
        v.setId(10L);
        v.setName("Mock Venue");
        given(venueRepository.findById(10L)).willReturn(Optional.of(v));

        Venue result = venueService.getVenueById(10L);
        assertNotNull(result);
        assertEquals("Mock Venue", result.getName());
    }

    @Test
    void testGetVenueById_NotFound() {
        given(venueRepository.findById(99L)).willReturn(Optional.empty());
        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> venueService.getVenueById(99L)
        );
        assertTrue(ex.getMessage().contains("Venue with id 99 not found"));
    }

    @Test
    void testCreateVenue() {
        // Arrange
        VenueRequest req = new VenueRequest("New Name", "City X", 500);
        Venue entity = new Venue();
        entity.setName("New Name");
        lenient().when(venueMapper.toEntity(req)).thenReturn(entity);

        Venue saved = new Venue();
        saved.setId(123L);
        saved.setName("New Name");
        lenient().when(venueRepository.save(any(Venue.class))).thenReturn(saved);

        // Act
        Venue result = venueService.createVenue(req);

        // Assert
        assertNotNull(result);
        assertEquals(123L, result.getId());
        assertEquals("New Name", result.getName());
    }

    @Test
    void testUpdateVenue_Found() {
        VenueRequest req = new VenueRequest("Updated", "City Y", 800);
        Venue existing = new Venue();
        existing.setId(5L);
        existing.setName("Old");
        lenient().when(venueRepository.findById(5L)).thenReturn(Optional.of(existing));
        lenient().when(venueRepository.save(any(Venue.class))).thenAnswer(inv -> inv.getArgument(0));

        Venue result = venueService.updateVenue(5L, req);
        assertEquals(5L, result.getId());
        assertEquals("Updated", result.getName());
        assertEquals("City Y", result.getLocation());
    }

    @Test
    void testUpdateVenue_NotFound() {
        lenient().when(venueRepository.findById(999L)).thenReturn(Optional.empty());
        VenueRequest req = new VenueRequest("Doesn’t", "Matter", 1);
        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> venueService.updateVenue(999L, req)
        );
        assertTrue(ex.getMessage().contains("Venue with id 999 not found"));
    }
}