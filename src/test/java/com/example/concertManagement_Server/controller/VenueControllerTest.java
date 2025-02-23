package com.example.concertManagement_Server.controller;

import com.example.concertManagement_Server.model.Venue;
import com.example.concertManagement_Server.service.VenueService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class VenueControllerTest {

    @Mock
    private VenueService venueService;

    @InjectMocks
    private VenueController venueController;

    @Test
    void testGetVenue_Found() {
        Venue mockVenue = new Venue();
        mockVenue.setId(100L);
        mockVenue.setName("Test Venue");

        when(venueService.getVenueById(100L)).thenReturn(mockVenue);

        ResponseEntity<Venue> response = venueController.getVenue(100L);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Test Venue", response.getBody().getName());
        verify(venueService).getVenueById(100L);
    }

    @Test
    void testGetVenue_NotFound() {
        when(venueService.getVenueById(999L)).thenReturn(null);

        ResponseEntity<Venue> response = venueController.getVenue(999L);
        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(venueService).getVenueById(999L);
    }
}
