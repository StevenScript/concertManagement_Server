package com.example.concertManagement_Server.repository;

import com.example.concertManagement_Server.model.Artist;
import com.example.concertManagement_Server.model.Venue;
import com.example.concertManagement_Server.model.Event;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
public class ArtistRepositoryTest {
    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private VenueRepository venueRepository;

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

    @Test
    void testFindArtistsByVenueId() {
        // 1) create a Venue
        Venue venue = new Venue();
        venue.setName("Test Venue");
        venue = venueRepository.save(venue);

        // 2) create an Artist
        Artist artist = new Artist();
        artist.setStageName("The Testers");
        artist = artistRepository.save(artist);

        // 3) create an Event linking the Venue + Artist
        Event event = new Event();
        event.setVenue(venue);
        eventRepository.save(event);
        event.getArtists().add(artist);
        eventRepository.save(event);

        // 4) call the method
        List<Artist> found = artistRepository.findArtistsByVenueId(venue.getId());
        Assertions.assertEquals(1, found.size());
        Assertions.assertEquals("The Testers", found.get(0).getStageName());
    }
}
