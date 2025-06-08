/**
 * VenueController.java
 *
 * REST controller responsible for managing venues and related data lookups.
 * Provides endpoints for:
 * - Full CRUD operations on venues
 * - Looking up artists booked at a venue
 * - Viewing upcoming events scheduled for a venue
 *
 * Works closely with:
 * - VenueService.java (venue logic)
 * - ArtistService.java (used for cross-linked queries)
 * - VenueMapper / ArtistMapper / EventMapper (entity <-> DTO mapping)
 * - VenueRequest / VenueDto / EventDto / ArtistDto (transport models)
 */
package com.example.concertManagement_Server.controller;

import com.example.concertManagement_Server.dto.*;
import com.example.concertManagement_Server.exception.ResourceNotFoundException;
import com.example.concertManagement_Server.mapper.*;
import com.example.concertManagement_Server.service.ArtistService;
import com.example.concertManagement_Server.service.VenueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/venues")
public class VenueController {

    private final VenueService venueService;
    private final ArtistService artistService;
    private final VenueMapper venueMapper;
    private final ArtistMapper artistMapper;
    private final EventMapper eventMapper;

    public VenueController(
            VenueService venueService,
            ArtistService artistService,
            VenueMapper venueMapper,
            ArtistMapper artistMapper,
            EventMapper eventMapper
    ) {
        this.venueService = venueService;
        this.artistService = artistService;
        this.venueMapper = venueMapper;
        this.artistMapper = artistMapper;
        this.eventMapper = eventMapper;
    }

    /* ─────────────────────────────
     *       Basic CRUD Endpoints
     * ───────────────────────────── */

    /**
     * Retrieves all venues in the system.
     *
     * @return list of VenueDto records
     */
    @GetMapping
    public ResponseEntity<List<VenueDto>> listAllVenues() {
        List<VenueDto> dtos = venueService.listAllVenues().stream()
                .map(venueMapper::toDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    /**
     * Retrieves a specific venue by ID.
     *
     * @param id the venue ID
     * @return VenueDto if found
     */
    @GetMapping("/{id}")
    public ResponseEntity<VenueDto> getVenue(@PathVariable Long id) {
        VenueDto dto = venueMapper.toDto(venueService.getVenueById(id));
        return ResponseEntity.ok(dto);
    }

    /**
     * Creates a new venue.
     *
     * @param req venue creation request payload
     * @return 201 Created with new VenueDto
     */
    @PostMapping
    public ResponseEntity<VenueDto> createVenue(@RequestBody VenueRequest req) {
        var saved = venueService.createVenue(req);
        return ResponseEntity.status(201).body(venueMapper.toDto(saved));
    }

    /**
     * Updates an existing venue.
     *
     * @param id  the venue ID
     * @param req updated venue fields
     * @return updated VenueDto
     */
    @PutMapping("/{id}")
    public ResponseEntity<VenueDto> updateVenue(
            @PathVariable Long id,
            @RequestBody VenueRequest req) {
        var updated = venueService.updateVenue(id, req);
        return ResponseEntity.ok(venueMapper.toDto(updated));
    }

    /* ───────────────────────────────────────────────
     *     Custom Lookups: Artists & Events at Venue
     * ─────────────────────────────────────────────── */

    /**
     * Retrieves all artists booked at a given venue.
     * Returned as ArtistDto list to prevent cyclic JSON issues.
     *
     * @param id the venue ID
     * @return list of ArtistDto objects
     */
    @GetMapping("/{id}/artists")
    public ResponseEntity<List<ArtistDto>> getArtistsForVenue(@PathVariable Long id) {
        List<ArtistDto> dtos = artistService.listAllArtistsForVenue(id).stream()
                .map(artistMapper::toDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    /**
     * Retrieves all upcoming events scheduled at a venue.
     *
     * @param id the venue ID
     * @return list of EventDto objects
     */
    @GetMapping("/{id}/upcoming-events")
    public ResponseEntity<List<EventDto>> getUpcomingEventsForVenue(@PathVariable Long id) {
        List<EventDto> dtos = venueService.findUpcomingEventsForVenue(id).stream()
                .map(eventMapper::toDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    /* ───────────────────────────────
     *          Delete Endpoint
     * ─────────────────────────────── */

    /**
     * Deletes a venue by ID.
     *
     * @param id the venue ID
     * @return 204 No Content if deleted, 404 if not found
     */
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
