package com.example.concertManagement_Server.controller;

import com.example.concertManagement_Server.model.Artist;
import com.example.concertManagement_Server.model.Event;
import com.example.concertManagement_Server.model.Venue;
import com.example.concertManagement_Server.service.ArtistService;
import com.example.concertManagement_Server.service.VenueService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/venues")
public class VenueController {

    private final VenueService venueService;
    private final ArtistService artistService;

    // Constructor-based dependency injection for required services
    public VenueController(VenueService venueService, ArtistService artistService) {
        this.venueService = venueService;
        this.artistService = artistService;
    }

    // Retrieves all venues from the database
    @GetMapping
    public ResponseEntity<List<Venue>> listAllVenues() {
        List<Venue> venues = venueService.listAllVenues();
        return ResponseEntity.ok(venues);
    }

    // Retrieves a specific venue by ID
    @GetMapping("/{id}")
    public ResponseEntity<Venue> getVenue(@PathVariable Long id) {
        Venue venue = venueService.getVenueById(id);
        if (venue == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(venue);
    }

    // Creates a new venue entry
    @PostMapping
    public ResponseEntity<Venue> createVenue(@RequestBody Venue venue) {
        Venue created = venueService.createVenue(venue);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // Updates an existing venue by ID
    @PutMapping("/{id}")
    public ResponseEntity<Venue> updateVenue(@PathVariable Long id, @RequestBody Venue venueData) {
        Venue updated = venueService.updateVenue(id, venueData);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    // Retrieves all artists associated with a specific venue
    @GetMapping("/{id}/artists")
    public ResponseEntity<List<Artist>> getArtistsForVenue(@PathVariable Long id) {
        List<Artist> artists = artistService.listAllArtistsForVenue(id);
        return ResponseEntity.ok(artists);
    }

    @GetMapping("/{id}/upcoming-events")
    public ResponseEntity<List<Event>> getUpcomingEventsForVenue(@PathVariable Long id) {
        List<Event> events = venueService.findUpcomingEventsForVenue(id);
        return ResponseEntity.ok(events);
    }
}
