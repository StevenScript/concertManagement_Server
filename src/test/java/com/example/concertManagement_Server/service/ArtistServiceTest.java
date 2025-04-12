package com.example.concertManagement_Server.service;

import com.example.concertManagement_Server.exception.ResourceNotFoundException;
import com.example.concertManagement_Server.model.Artist;
import com.example.concertManagement_Server.repository.ArtistRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ArtistServiceTest {

    @Mock
    private ArtistRepository artistRepository;

    @InjectMocks
    private ArtistService artistService;

    @Test
    void testGetArtistById_Found() {
        Artist mockArtist = new Artist();
        mockArtist.setId(1L);
        mockArtist.setStageName("Mock Artist");

        when(artistRepository.findById(1L)).thenReturn(Optional.of(mockArtist));

        Artist result = artistService.getArtistById(1L);
        Assertions.assertNotNull(result, "Artist should not be null when found");
        Assertions.assertEquals("Mock Artist", result.getStageName());
        verify(artistRepository, times(1)).findById(1L);
    }

    @Test
    void testGetArtistById_NotFound() {
        when(artistRepository.findById(999L)).thenReturn(Optional.empty());

        ResourceNotFoundException thrown = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> artistService.getArtistById(999L),
                "Expected ResourceNotFoundException for nonexistent artist"
        );
        Assertions.assertEquals("Artist with id 999 not found", thrown.getMessage());
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

        when(artistRepository.save(newArtist)).thenReturn(savedArtist);

        Artist result = artistService.createArtist(newArtist);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(10L, result.getId());
        Assertions.assertEquals("New Artist", result.getStageName());

        verify(artistRepository, times(1)).save(newArtist);
    }

    @Test
    void testUpdateArtist_Found() {
        Artist existing = new Artist();
        existing.setId(1L);
        existing.setStageName("Old Name");
        existing.setGenre("Rock");

        when(artistRepository.findById(1L)).thenReturn(Optional.of(existing));

        Artist updatedData = new Artist();
        updatedData.setStageName("New Name");
        updatedData.setGenre("Pop");

        when(artistRepository.save(any(Artist.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Artist result = artistService.updateArtist(1L, updatedData);
        Assertions.assertNotNull(result, "Should return the updated Artist");
        Assertions.assertEquals("New Name", result.getStageName());
        Assertions.assertEquals("Pop", result.getGenre());

        verify(artistRepository).findById(1L);
        verify(artistRepository).save(any(Artist.class));
    }

    @Test
    void testUpdateArtist_NotFound() {
        when(artistRepository.findById(999L)).thenReturn(Optional.empty());

        Artist updatedData = new Artist();
        updatedData.setStageName("Doesn't matter");

        ResourceNotFoundException thrown = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> artistService.updateArtist(999L, updatedData),
                "Expected ResourceNotFoundException when updating nonexistent artist"
        );
        Assertions.assertEquals("Artist with id 999 not found", thrown.getMessage());
        verify(artistRepository).findById(999L);
        verify(artistRepository, never()).save(any(Artist.class));
    }

    @Test
    void testListAllArtistsForVenue() {
        Artist mockArtist = new Artist();
        mockArtist.setId(1L);
        mockArtist.setStageName("The Testers");

        when(artistRepository.findArtistsByVenueId(10L))
                .thenReturn(Collections.singletonList(mockArtist));

        List<Artist> results = artistService.listAllArtistsForVenue(10L);
        Assertions.assertEquals(1, results.size());
        Assertions.assertEquals("The Testers", results.get(0).getStageName());

        verify(artistRepository).findArtistsByVenueId(10L);
    }
}

