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
public class EventRepositoryTest {

    @Autowired private EventRepository eventRepository;
    @Autowired private VenueRepository venueRepository;
    @Autowired private ArtistRepository artistRepository;

    @Test
    void testSaveAndFindEvent() {
        Venue venue = new Venue();
        venue.setName("Sample Venue");
        venue.setLocation("Sample City");
        venue.setCapacity(1000);
        Venue savedVenue = venueRepository.save(venue);

        Event event = new Event();
        event.setName("RepoSave");
        event.setEventDate(LocalDate.of(2025, 5, 10));
        event.setTicketPrice(59.99);
        event.setAvailableTickets(500);
        event.setVenue(savedVenue);

        Event savedEvent = eventRepository.save(event);

        Assertions.assertNotNull(savedEvent.getId());

        Event foundEvent = eventRepository.findById(savedEvent.getId()).orElse(null);
        Assertions.assertNotNull(foundEvent);
        Assertions.assertEquals(LocalDate.of(2025, 5, 10), foundEvent.getEventDate());
        Assertions.assertEquals(59.99, foundEvent.getTicketPrice());
        Assertions.assertEquals(500, foundEvent.getAvailableTickets());
        Assertions.assertEquals(savedVenue.getId(), foundEvent.getVenue().getId());
    }

    @Test
    void testFindEventsByArtistId() {
        Artist artist = new Artist();
        artist.setStageName("Test Artist");
        artist = artistRepository.save(artist);

        Event event = new Event();
        event.setName("ArtistJoin");
        event.setEventDate(LocalDate.of(2025, 5, 10));
        event.setTicketPrice(50.0);
        event.getArtists().add(artist);
        eventRepository.save(event);

        List<Event> foundEvents = eventRepository.findEventsByArtistId(artist.getId());
        Assertions.assertEquals(1, foundEvents.size());
        Assertions.assertEquals(LocalDate.of(2025, 5, 10), foundEvents.get(0).getEventDate());
    }
}
