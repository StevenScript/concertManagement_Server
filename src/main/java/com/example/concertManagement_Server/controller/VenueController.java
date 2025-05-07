package com.example.concertManagement_Server.controller;

import com.example.concertManagement_Server.dto.VenueDto;
import com.example.concertManagement_Server.dto.VenueRequest;
import com.example.concertManagement_Server.exception.ResourceNotFoundException;
import com.example.concertManagement_Server.mapper.VenueMapper;
import com.example.concertManagement_Server.model.Artist;
import com.example.concertManagement_Server.model.Event;
import com.example.concertManagement_Server.service.ArtistService;
import com.example.concertManagement_Server.service.VenueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/venues")
public class VenueController {

    private final VenueService venueService;
    private final ArtistService artistService;
    private final VenueMapper venueMapper;

    public VenueController(VenueService venueService,
                           ArtistService artistService,
                           VenueMapper venueMapper) {
        this.venueService = venueService;
        this.artistService = artistService;
        this.venueMapper = venueMapper;
    }

    /**
     * Retrieves all venues.
     */
    @GetMapping
    public ResponseEntity<List<VenueDto>> listAllVenues() {
        var dtos = venueService.listAllVenues().stream()
                .map(venueMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /**
     * Retrieves a single venue by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<VenueDto> getVenue(@PathVariable Long id) {
        VenueDto dto = venueMapper.toDto(venueService.getVenueById(id));
        return ResponseEntity.ok(dto);
    }

    /**
     * Creates a new venue.
     */
    @PostMapping
    public ResponseEntity<VenueDto> createVenue(@RequestBody VenueRequest req) {
        var saved = venueService.createVenue(req);
        return ResponseEntity.status(201).body(venueMapper.toDto(saved));
    }

    /**
     * Updates an existing venue.
     */
    @PutMapping("/{id}")
    public ResponseEntity<VenueDto> updateVenue(
            @PathVariable Long id,
            @RequestBody VenueRequest req) {
        var updated = venueService.updateVenue(id, req);
        return ResponseEntity.ok(venueMapper.toDto(updated));
    }

    /**
     * Lists all artists scheduled for a venue.
     */
    @GetMapping("/{id}/artists")
    public ResponseEntity<List<Artist>> getArtistsForVenue(@PathVariable Long id) {
        return ResponseEntity.ok(
                artistService.listAllArtistsForVenue(id)
        );
    }

    /**
     * Retrieves upcoming events for a venue.
     */
    @GetMapping("/{id}/upcoming-events")
    public ResponseEntity<List<Event>> getUpcomingEventsForVenue(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                venueService.findUpcomingEventsForVenue(id)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVenue(@PathVariable Long id) {
        try {
            venueService.deleteVenue(id);
            return ResponseEntity.noContent().build();   // 204
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();    // 404
        }
    }
}
