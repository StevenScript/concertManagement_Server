package com.example.concertManagement_Server.service;

import com.example.concertManagement_Server.model.Venue;
import com.example.concertManagement_Server.repository.VenueRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VenueServiceTest {

    @Mock
    private VenueRepository venueRepository;

    @InjectMocks
    private VenueService venueService;

    @Test
    void testGetVenueById_Found() {
        Venue mockVenue = new Venue();
        mockVenue.setId(10L);
        mockVenue.setName("Mock Venue");

        when(venueRepository.findById(10L)).thenReturn(Optional.of(mockVenue));

        Venue result = venueService.getVenueById(10L);
        Assertions.assertNotNull(result);
        Assertions.assertEquals("Mock Venue", result.getName());
        verify(venueRepository).findById(10L);
    }

    @Test
    void testGetVenueById_NotFound() {
        when(venueRepository.findById(99L)).thenReturn(Optional.empty());

        Venue result = venueService.getVenueById(99L);
        Assertions.assertNull(result, "Null if venue not found");
        verify(venueRepository).findById(99L);
    }
}
