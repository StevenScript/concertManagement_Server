package com.example.concertManagement_Server.repository;

import com.example.concertManagement_Server.model.Artist;
import com.example.concertManagement_Server.model.Event;
import com.example.concertManagement_Server.model.Venue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

@DataJpaTest
@ActiveProfiles("test")
public class ArtistRepositoryTest {

    @Autowired private ArtistRepository artistRepository;
    @Autowired private EventRepository eventRepository;
    @Autowired private VenueRepository venueRepository;

    @Test
    void testSaveAndFindArtist() {
        Artist artist = new Artist();
        artist.setStageName("Test Artist");
        artist.setGenre("Rock");
        artist.setMembersCount(4);
        artist.setHomeCity("Test City");

        Artist savedArtist = artistRepository.save(artist);

        Assertions.assertNotNull(savedArtist.getId());

        Artist foundArtist = artistRepository.findById(savedArtist.getId()).orElse(null);
        Assertions.assertNotNull(foundArtist);
        Assertions.assertEquals("Test Artist", foundArtist.getStageName());
        Assertions.assertEquals("Rock", foundArtist.getGenre());
        Assertions.assertEquals(4, foundArtist.getMembersCount());
        Assertions.assertEquals("Test City", foundArtist.getHomeCity());
    }

    @Test
    void testFindArtistsByVenueId() {
        // Venue
        Venue venue = new Venue();
        venue.setName("Test Venue");
        venue = venueRepository.save(venue);

        // Artist
        Artist artist = new Artist();
        artist.setStageName("The Testers");
        artist = artistRepository.save(artist);

        // Event linking venue + artist
        Event event = new Event();
        event.setName("VenueEvent");
        event.setEventDate(LocalDate.now().plusDays(10));
        event.setVenue(venue);
        eventRepository.save(event);

        event.getArtists().add(artist);
        eventRepository.save(event);

        List<Artist> found = artistRepository.findArtistsByVenueId(venue.getId());
        Assertions.assertEquals(1, found.size());
        Assertions.assertEquals("The Testers", found.get(0).getStageName());
    }
}
