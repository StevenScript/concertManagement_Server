package com.example.concertManagement_Server.repository;

import com.example.concertManagement_Server.model.Artist;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class ArtistRepositoryTest {
    @Autowired
    private ArtistRepository artistRepository;

    @Test
    void testSaveAndFindArtist() {
        // 1. Create a new Artist object
        Artist artist = new Artist();
        artist.setStageName("Test Artist");
        artist.setGenre("Rock");
        artist.setMembersCount(4);
        artist.setHomeCity("Test City");

        // 2. Save it using the repository
        Artist savedArtist = artistRepository.save(artist);

        // 3. Verify it's assigned an ID
        Assertions.assertNotNull(savedArtist.getId(),
                "Saved artist should have an auto-generated ID after save");

        // 4. Retrieve the artist from DB by its new ID
        Artist foundArtist = artistRepository
                .findById(savedArtist.getId())
                .orElse(null);

        // 5. Verify that we actually found it, and the fields match
        Assertions.assertNotNull(foundArtist, "Artist should exist in the database");
        Assertions.assertEquals("Test Artist", foundArtist.getStageName());
        Assertions.assertEquals("Rock", foundArtist.getGenre());
        Assertions.assertEquals(4, foundArtist.getMembersCount());
        Assertions.assertEquals("Test City", foundArtist.getHomeCity());
    }
}
