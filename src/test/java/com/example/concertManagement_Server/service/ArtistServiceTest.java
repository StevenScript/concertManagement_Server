package com.example.concertManagement_Server.service;

import com.example.concertManagement_Server.exception.ResourceNotFoundException;
import com.example.concertManagement_Server.model.Artist;
import com.example.concertManagement_Server.repository.ArtistRepository;
import com.example.concertManagement_Server.repository.EventRepository;
import com.example.concertManagement_Server.repository.TicketRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ArtistServiceTest {

    @Mock
    private ArtistRepository artistRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private ArtistService artistService;

    @Test
    void testGetArtistById_Found() {
        Artist mockArtist = new Artist();
        mockArtist.setId(1L);
        mockArtist.setStageName("Mock Artist");

        given(artistRepository.findById(1L)).willReturn(Optional.of(mockArtist));

        Artist result = artistService.getArtistById(1L);
        assertNotNull(result, "Artist should not be null when found");
        assertEquals("Mock Artist", result.getStageName());
        verify(artistRepository, times(1)).findById(1L);
    }

    @Test
    void testGetArtistById_NotFound() {
        given(artistRepository.findById(999L)).willReturn(Optional.empty());

        ResourceNotFoundException thrown = assertThrows(
                ResourceNotFoundException.class,
                () -> artistService.getArtistById(999L),
                "Expected ResourceNotFoundException for nonexistent artist"
        );
        assertEquals("Artist with id 999 not found", thrown.getMessage());
        verify(artistRepository).findById(999L);
    }

    @Test
    void testCreateArtist() {
        Artist newArtist = new Artist();
        newArtist.setStageName("New Artist");
        newArtist.setGenre("Rock");

        Artist savedArtist = new Artist();
        savedArtist.setId(10L);
        savedArtist.setStageName("New Artist");
        savedArtist.setGenre("Rock");

        given(artistRepository.save(newArtist)).willReturn(savedArtist);

        Artist result = artistService.createArtist(newArtist);
        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals("New Artist", result.getStageName());

        verify(artistRepository, times(1)).save(newArtist);
    }

    @Test
    void testUpdateArtist_Found() {
        Artist existing = new Artist();
        existing.setId(1L);
        existing.setStageName("Old Name");
        existing.setGenre("Rock");

        given(artistRepository.findById(1L)).willReturn(Optional.of(existing));
        given(artistRepository.save(any(Artist.class))).willAnswer(inv -> inv.getArgument(0));

        Artist updatedData = new Artist();
        updatedData.setStageName("New Name");
        updatedData.setGenre("Pop");

        Artist result = artistService.updateArtist(1L, updatedData);
        assertNotNull(result, "Should return the updated Artist");
        assertEquals("New Name", result.getStageName());
        assertEquals("Pop", result.getGenre());

        verify(artistRepository).findById(1L);
        verify(artistRepository).save(any(Artist.class));
    }

    @Test
    void testUpdateArtist_NotFound() {
        given(artistRepository.findById(999L)).willReturn(Optional.empty());

        Artist updatedData = new Artist();
        updatedData.setStageName("Doesn't matter");

        ResourceNotFoundException thrown = assertThrows(
                ResourceNotFoundException.class,
                () -> artistService.updateArtist(999L, updatedData),
                "Expected ResourceNotFoundException when updating nonexistent artist"
        );
        assertEquals("Artist with id 999 not found", thrown.getMessage());
        verify(artistRepository).findById(999L);
        verify(artistRepository, never()).save(any(Artist.class));
    }

    @Test
    void testListAllArtistsForVenue() {
        Artist mockArtist = new Artist();
        mockArtist.setId(1L);
        mockArtist.setStageName("The Testers");

        given(artistRepository.findArtistsByVenueId(10L))
                .willReturn(Collections.singletonList(mockArtist));

        var results = artistService.listAllArtistsForVenue(10L);
        assertEquals(1, results.size());
        assertEquals("The Testers", results.get(0).getStageName());

        verify(artistRepository).findArtistsByVenueId(10L);
    }
}
