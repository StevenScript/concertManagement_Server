package com.example.concertManagement_Server.repository;

import com.example.concertManagement_Server.model.Venue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
public class VenueRepositoryTest {
    @Autowired
    private VenueRepository venueRepository;

    @Test
    void testSaveAndFindVenue() {
        // Create a new Venue object
        Venue venue = new Venue();
        venue.setName("Test Venue");
        venue.setLocation("Test Location");
        venue.setCapacity(500);

        // Save it using the repository
        Venue savedVenue = venueRepository.save(venue);

        // Verify it got an ID
        Assertions.assertNotNull(savedVenue.getId());

        // Retrieve it by the new ID
        Venue foundVenue = venueRepository.findById(savedVenue.getId()).orElse(null);
        Assertions.assertNotNull(foundVenue, "Should retrieve the saved venue");
        Assertions.assertEquals("Test Venue", foundVenue.getName());
        Assertions.assertEquals("Test Location", foundVenue.getLocation());
        Assertions.assertEquals(500, foundVenue.getCapacity());
    }
}
