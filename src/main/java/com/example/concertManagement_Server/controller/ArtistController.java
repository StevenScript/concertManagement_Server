package com.example.concertManagement_Server.controller;

import com.example.concertManagement_Server.model.Artist;
import com.example.concertManagement_Server.model.Event;
import com.example.concertManagement_Server.service.ArtistService;
import com.example.concertManagement_Server.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/artists")
public class ArtistController {

    private final ArtistService artistService;
    private final EventService eventService;

    // Constructor-based dependency injection for required services
    public ArtistController(ArtistService artistService, EventService eventService) {
        this.artistService = artistService;
        this.eventService = eventService;
    }

    // Retrieves all artists from the database
    @GetMapping
    public List<Artist> getAllArtists() {
        return artistService.getAllArtists();
    }

    // Retrieves a specific artist by ID
    @GetMapping("/{id}")
    public ResponseEntity<Artist> getArtist(@PathVariable Long id) {
        Artist artist = artistService.getArtistById(id);
        if (artist == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(artist);
    }

    // Creates a new artist entry
    @PostMapping
    public ResponseEntity<Artist> createArtist(@RequestBody Artist artist) {
        Artist created = artistService.createArtist(artist);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // Updates an existing artist's details by ID
    @PutMapping("/{id}")
    public ResponseEntity<Artist> updateArtist(@PathVariable Long id, @RequestBody Artist artistData) {
        Artist updated = artistService.updateArtist(id, artistData);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    // Retrieves all events associated with a specific artist by ID
    @GetMapping("/{id}/events")
    public ResponseEntity<List<Event>> getEventsForArtist(@PathVariable Long id) {
        List<Event> events = eventService.listAllEventsForArtist(id);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{artistId}/ticket-count")
    public ResponseEntity<Long> getTicketCountForArtist(@PathVariable Long artistId) {
        Long count = artistService.getTicketCountForArtist(artistId);
        return ResponseEntity.ok(count);
    }
}
