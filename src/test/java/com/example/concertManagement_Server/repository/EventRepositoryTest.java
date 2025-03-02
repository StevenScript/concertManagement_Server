package com.example.concertManagement_Server.repository;

import com.example.concertManagement_Server.model.Artist;
import com.example.concertManagement_Server.model.Event;
import com.example.concertManagement_Server.model.Venue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

@DataJpaTest
public class EventRepositoryTest {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private VenueRepository venueRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Test
    void testSaveAndFindEvent() {
        // Create a Venue
        Venue venue = new Venue();
        venue.setName("Sample Venue");
        venue.setLocation("Sample City");
        venue.setCapacity(1000);

        Venue savedVenue = venueRepository.save(venue);

        // Create a new Event object
        Event event = new Event();
        event.setEventDate(LocalDate.of(2025, 5, 10));
        event.setTicketPrice(59.99);
        event.setAvailableTickets(500);
        event.setVenue(savedVenue); // link to our savedVenue

        // Save it using the repository
        Event savedEvent = eventRepository.save(event);

        // Verify ID is assigned
        Assertions.assertNotNull(savedEvent.getId(), "Event should have an auto-generated ID");

        // Retrieve it by ID
        Event foundEvent = eventRepository.findById(savedEvent.getId()).orElse(null);
        Assertions.assertNotNull(foundEvent, "The event should be found in the database");
        Assertions.assertEquals(LocalDate.of(2025, 5, 10), foundEvent.getEventDate());
        Assertions.assertEquals(59.99, foundEvent.getTicketPrice());
        Assertions.assertEquals(500, foundEvent.getAvailableTickets());
        Assertions.assertEquals(savedVenue.getId(), foundEvent.getVenue().getId());
    }

    @Test
    void testFindEventsByArtistId() {
        // Create an Artist
        Artist artist = new Artist();
        artist.setStageName("Test Artist");
        artist = artistRepository.save(artist);

        // Create an Event referencing that artist
        Event event = new Event();
        event.setEventDate(LocalDate.of(2025, 5, 10));
        event.setTicketPrice(50.0);
        eventRepository.save(event);
        event.getArtists().add(artist);
        eventRepository.save(event);

        // Call the method we want to test
        List<Event> foundEvents = eventRepository.findEventsByArtistId(artist.getId());
        Assertions.assertEquals(1, foundEvents.size());
        Assertions.assertEquals(LocalDate.of(2025, 5, 10), foundEvents.get(0).getEventDate());
    }
}
