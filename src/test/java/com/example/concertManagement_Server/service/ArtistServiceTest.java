package com.example.concertManagement_Server.service;

import com.example.concertManagement_Server.model.Artist;
import com.example.concertManagement_Server.repository.ArtistRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ArtistServiceTest {

    @Mock
    private ArtistRepository artistRepository;

    @InjectMocks
    private ArtistService artistService;


    @Test
    void testGetArtistById_Found() {
        // Create a mock Artist object
        Artist mockArtist = new Artist();
        mockArtist.setId(1L);
        mockArtist.setStageName("Mock Artist");

        // Define the behavior of the mocked repository
        when(artistRepository.findById(1L)).thenReturn(Optional.of(mockArtist));

        // Call the service method
        Artist result = artistService.getArtistById(1L);

        // Verify the result
        Assertions.assertNotNull(result, "Artist should not be null when found");
        Assertions.assertEquals("Mock Artist", result.getStageName());

        // Verify repository was called
        verify(artistRepository, times(1)).findById(1L);
    }

    @Test
    void testGetArtistById_NotFound() {
        // The repository returns an empty Optional
        when(artistRepository.findById(999L)).thenReturn(Optional.empty());

        // Call service with a non-existing artist ID
        Artist result = artistService.getArtistById(999L);

        // Expect null or an exception (implementation choice).
        Assertions.assertNull(result, "Artist should be null if not found");

        // Verify the repository was called
        verify(artistRepository).findById(999L);
    }

    @Test
    void testCreateArtist() {
        // Prepare an Artist to save
        Artist newArtist = new Artist();
        newArtist.setStageName("New Artist");
        newArtist.setGenre("Rock");

        // The repository will assign an ID, returning the same object
        Artist savedArtist = new Artist();
        savedArtist.setId(10L);
        savedArtist.setStageName("New Artist");
        savedArtist.setGenre("Rock");

        // Mock the repository's save method
        when(artistRepository.save(newArtist)).thenReturn(savedArtist);

        // Call the service method
        Artist result = artistService.createArtist(newArtist);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(10L, result.getId());
        Assertions.assertEquals("New Artist", result.getStageName());

        // Verify the repository was used
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

        Artist result = artistService.updateArtist(999L, updatedData);
        Assertions.assertNull(result, "Should return null if no such artist");
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
