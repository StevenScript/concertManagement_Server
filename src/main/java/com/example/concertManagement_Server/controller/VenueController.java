package com.example.concertManagement_Server.controller;

import com.example.concertManagement_Server.dto.*;
import com.example.concertManagement_Server.exception.ResourceNotFoundException;
import com.example.concertManagement_Server.mapper.*;
import com.example.concertManagement_Server.service.ArtistService;
import com.example.concertManagement_Server.service.VenueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/venues")
public class VenueController {

    private final VenueService  venueService;
    private final ArtistService artistService;
    private final VenueMapper   venueMapper;
    private final ArtistMapper  artistMapper;   // ← NEW
    private final EventMapper   eventMapper;    // ← NEW (optional)

    public VenueController(
            VenueService  venueService,
            ArtistService artistService,
            VenueMapper   venueMapper,
            ArtistMapper  artistMapper,         // ← NEW
            EventMapper   eventMapper           // ← NEW
    ) {
        this.venueService  = venueService;
        this.artistService = artistService;
        this.venueMapper   = venueMapper;
        this.artistMapper  = artistMapper;      // ← NEW
        this.eventMapper   = eventMapper;       // ← NEW
    }

    /* ---------- basic CRUD ---------- */

    @GetMapping
    public ResponseEntity<List<VenueDto>> listAllVenues() {
        List<VenueDto> dtos = venueService.listAllVenues().stream()
                .map(venueMapper::toDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VenueDto> getVenue(@PathVariable Long id) {
        VenueDto dto = venueMapper.toDto(venueService.getVenueById(id));
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<VenueDto> createVenue(@RequestBody VenueRequest req) {
        var saved = venueService.createVenue(req);
        return ResponseEntity.status(201).body(venueMapper.toDto(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VenueDto> updateVenue(
            @PathVariable Long id,
            @RequestBody VenueRequest req) {
        var updated = venueService.updateVenue(id, req);
        return ResponseEntity.ok(venueMapper.toDto(updated));
    }

    /* ---------- extra look-ups ---------- */

    /** Artists booked at this venue (as DTOs, avoids cyclic JSON). */
    @GetMapping("/{id}/artists")
    public ResponseEntity<List<ArtistDto>> getArtistsForVenue(@PathVariable Long id) {
        List<ArtistDto> dtos = artistService.listAllArtistsForVenue(id).stream()
                .map(artistMapper::toDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    /** Upcoming events at this venue (mapped to DTOs for consistency). */
    @GetMapping("/{id}/upcoming-events")
    public ResponseEntity<List<EventDto>> getUpcomingEventsForVenue(@PathVariable Long id) {
        List<EventDto> dtos = venueService.findUpcomingEventsForVenue(id).stream()
                .map(eventMapper::toDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    /* ---------- delete ---------- */

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVenue(@PathVariable Long id) {
        try {
            venueService.deleteVenue(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
