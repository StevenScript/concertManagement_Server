package com.example.concertManagement_Server.controller;

import com.example.concertManagement_Server.model.Artist;
import com.example.concertManagement_Server.model.Venue;
import com.example.concertManagement_Server.service.ArtistService;
import com.example.concertManagement_Server.service.VenueService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class VenueControllerTest {

    @Mock
    private VenueService venueService;

    @Mock
    private ArtistService artistService;

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

    @Test
    void testCreateVenue() {
        Venue newVenue = new Venue();
        newVenue.setName("New Venue");

        Venue savedVenue = new Venue();
        savedVenue.setId(777L);
        savedVenue.setName("New Venue");

        when(venueService.createVenue(newVenue)).thenReturn(savedVenue);

        ResponseEntity<Venue> response = venueController.createVenue(newVenue);
        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(777L, response.getBody().getId());
        verify(venueService).createVenue(newVenue);
    }

    @Test
    void testUpdateVenue_Found() {
        Venue updatedData = new Venue();
        updatedData.setName("Updated Name");

        Venue updatedVenue = new Venue();
        updatedVenue.setId(5L);
        updatedVenue.setName("Updated Name");

        when(venueService.updateVenue(5L, updatedData)).thenReturn(updatedVenue);

        ResponseEntity<Venue> response = venueController.updateVenue(5L, updatedData);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Updated Name", response.getBody().getName());
        verify(venueService).updateVenue(5L, updatedData);
    }

    @Test
    void testUpdateVenue_NotFound() {
        Venue updatedData = new Venue();
        updatedData.setName("Nope");

        when(venueService.updateVenue(999L, updatedData)).thenReturn(null);

        ResponseEntity<Venue> response = venueController.updateVenue(999L, updatedData);
        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(venueService).updateVenue(999L, updatedData);
    }

    @Test
    void testGetArtistsForVenue() {
        Artist mockArtist = new Artist();
        mockArtist.setId(100L);
        mockArtist.setStageName("The Testers");

        when(artistService.listAllArtistsForVenue(5L))
                .thenReturn(Collections.singletonList(mockArtist));

        ResponseEntity<List<Artist>> response = venueController.getArtistsForVenue(5L);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(100L, response.getBody().get(0).getId());

        verify(artistService).listAllArtistsForVenue(5L);
    }
}
