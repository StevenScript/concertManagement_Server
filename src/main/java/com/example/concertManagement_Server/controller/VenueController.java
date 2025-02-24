package com.example.concertManagement_Server.controller;

import com.example.concertManagement_Server.model.Artist;
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

    public VenueController(VenueService venueService, ArtistService artistService) {

        this.venueService = venueService;
        this.artistService = artistService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venue> getVenue(@PathVariable Long id) {
        Venue venue = venueService.getVenueById(id);
        if (venue == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(venue);
    }

    @PostMapping
    public ResponseEntity<Venue> createVenue(@RequestBody Venue venue) {
        Venue created = venueService.createVenue(venue);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Venue> updateVenue(@PathVariable Long id, @RequestBody Venue venueData) {
        Venue updated = venueService.updateVenue(id, venueData);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}/artists")
    public ResponseEntity<List<Artist>> getArtistsForVenue(@PathVariable Long id) {
        List<Artist> artists = artistService.listAllArtistsForVenue(id);
        return ResponseEntity.ok(artists);
    }
}
