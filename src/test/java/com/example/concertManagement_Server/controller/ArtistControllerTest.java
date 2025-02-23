package com.example.concertManagement_Server.controller;

import com.example.concertManagement_Server.model.Artist;
import com.example.concertManagement_Server.service.ArtistService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ArtistControllerTest {

    @Mock
    private ArtistService artistService;

    @InjectMocks
    private ArtistController artistController;

    @Test
    void testGetArtist_Found() {
        // Service returns an Artist for ID=1
        Artist mockArtist = new Artist();
        mockArtist.setId(1L);
        mockArtist.setStageName("Mocked Artist");

        when(artistService.getArtistById(1L)).thenReturn(mockArtist);

        // Controller method to test:
        ResponseEntity<Artist> response = artistController.getArtist(1L);

        assertNotNull(response, "ResponseEntity should not be null");
        assertEquals(200, response.getStatusCodeValue(), "Should return 200 OK");
        assertNotNull(response.getBody(), "Body should contain an Artist");
        assertEquals("Mocked Artist", response.getBody().getStageName());

        verify(artistService).getArtistById(1L);
    }

    @Test
    void testGetArtist_NotFound() {
        when(artistService.getArtistById(999L)).thenReturn(null);

        ResponseEntity<Artist> response = artistController.getArtist(999L);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue(), "Should return 404 if not found");
        assertNull(response.getBody(), "No body if not found");

        verify(artistService).getArtistById(999L);
    }

    @Test
    void testCreateArtist() {
        Artist newArtist = new Artist();
        newArtist.setStageName("StevenWonder");

        Artist savedArtist = new Artist();
        savedArtist.setId(10L);
        savedArtist.setStageName("StevenWonder");

        when(artistService.createArtist(newArtist)).thenReturn(savedArtist);

        ResponseEntity<Artist> response = artistController.createArtist(newArtist);
        assertNotNull(response);
        assertEquals(201, response.getStatusCodeValue(), "Should return 201 Created");
        assertNotNull(response.getBody());
        assertEquals(10L, response.getBody().getId());
        assertEquals("StevenWonder", response.getBody().getStageName());

        verify(artistService).createArtist(newArtist);
    }

    @Test
    void testUpdateArtist() {
        Artist existing = new Artist();
        existing.setId(2L);
        existing.setStageName("Old Name");

        Artist updatedData = new Artist();
        updatedData.setStageName("New Name");

        Artist updatedResult = new Artist();
        updatedResult.setId(2L);
        updatedResult.setStageName("New Name");

        when(artistService.updateArtist(2L, updatedData)).thenReturn(updatedResult);

        ResponseEntity<Artist> response = artistController.updateArtist(2L, updatedData);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("New Name", response.getBody().getStageName());

        verify(artistService).updateArtist(2L, updatedData);
    }

    @Test
    void testUpdateArtist_NotFound() {
        Artist updatedData = new Artist();
        updatedData.setStageName("Does not matter");

        when(artistService.updateArtist(999L, updatedData)).thenReturn(null);

        ResponseEntity<Artist> response = artistController.updateArtist(999L, updatedData);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());

        verify(artistService).updateArtist(999L, updatedData);
    }
}
