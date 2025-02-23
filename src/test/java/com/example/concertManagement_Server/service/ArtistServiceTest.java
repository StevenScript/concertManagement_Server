package com.example.concertManagement_Server.service;

import com.example.concertManagement_Server.model.Artist;
import com.example.concertManagement_Server.repository.ArtistRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
public class ArtistServiceTest {

    @Mock
    private ArtistRepository artistRepository;  // We'll mock this

    @InjectMocks
    private ArtistService artistService;        // This is what we're testing (doesn't exist yet)

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetArtistById_Found() {
        // 1. Create a mock Artist object
        Artist mockArtist = new Artist();
        mockArtist.setId(1L);
        mockArtist.setStageName("Mock Artist");

        // 2. Define the behavior of the mocked repository
        when(artistRepository.findById(1L)).thenReturn(Optional.of(mockArtist));

        // 3. Call the service method
        Artist result = artistService.getArtistById(1L);

        // 4. Verify the result
        Assertions.assertNotNull(result, "Artist should not be null when found");
        Assertions.assertEquals("Mock Artist", result.getStageName());

        // 5. Verify repository was called
        verify(artistRepository, times(1)).findById(1L);
    }

    @Test
    void testGetArtistById_NotFound() {
        // The repository returns an empty Optional
        when(artistRepository.findById(999L)).thenReturn(Optional.empty());

        // 1. Call service with a non-existing artist ID
        Artist result = artistService.getArtistById(999L);

        // 2. We expect null or an exception (implementation choice).
        Assertions.assertNull(result, "Artist should be null if not found");

        // 3. Verify the repository was called
        verify(artistRepository).findById(999L);
    }

    @Test
    void testCreateArtist() {
        // 1. Prepare an Artist to save
        Artist newArtist = new Artist();
        newArtist.setStageName("New Artist");
        newArtist.setGenre("Rock");

        // 2. The repository will assign an ID, returning the same object
        Artist savedArtist = new Artist();
        savedArtist.setId(10L);
        savedArtist.setStageName("New Artist");
        savedArtist.setGenre("Rock");

        // 3. Mock the repository's save method
        when(artistRepository.save(newArtist)).thenReturn(savedArtist);

        // 4. Call the service method
        Artist result = artistService.createArtist(newArtist);

        // 5. Validate
        Assertions.assertNotNull(result);
        Assertions.assertEquals(10L, result.getId());
        Assertions.assertEquals("New Artist", result.getStageName());

        // 6. Verify the repository was used
        verify(artistRepository, times(1)).save(newArtist);
    }
}
