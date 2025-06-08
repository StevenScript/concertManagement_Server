/**
 * ArtistController.java
 *
 * REST controller responsible for handling all endpoints related to artist operations.
 * Supports full CRUD operations, as well as additional endpoints to query:
 * - Events associated with a given artist
 * - Venues the artist performs at
 * - Total ticket sales for the artist
 *
 * Works closely with:
 * - ArtistService.java (business logic)
 * - EventService.java (for artist-event relationship lookups)
 * - ArtistMapper.java (for mapping between Artist entities and DTOs)
 * - ArtistRequest / ArtistDto (input/output models)
 */
package com.example.concertManagement_Server.controller;

import com.example.concertManagement_Server.dto.ArtistDto;
import com.example.concertManagement_Server.dto.ArtistRequest;
import com.example.concertManagement_Server.exception.ResourceNotFoundException;
import com.example.concertManagement_Server.mapper.ArtistMapper;
import com.example.concertManagement_Server.model.Event;
import com.example.concertManagement_Server.model.Venue;
import com.example.concertManagement_Server.service.ArtistService;
import com.example.concertManagement_Server.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/artists")
public class ArtistController {

    private final ArtistService artistService;
    private final EventService eventService;
    private final ArtistMapper artistMapper;

    public ArtistController(ArtistService artistService,
                            EventService eventService,
                            ArtistMapper artistMapper) {
        this.artistService = artistService;
        this.eventService = eventService;
        this.artistMapper = artistMapper;
    }

    /**
     * Retrieves all artists in the system.
     *
     * @return List of all artists as ArtistDto objects
     */
    @GetMapping
    public List<ArtistDto> getAllArtists() {
        return artistService.getAllArtists().stream()
                .map(artistMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a single artist by their unique ID.
     *
     * @param id the ID of the artist
     * @return ArtistDto if found, or 404 Not Found if not
     */
    @GetMapping("/{id}")
    public ResponseEntity<ArtistDto> getArtist(@PathVariable Long id) {
        var artist = artistService.getArtistById(id);
        return ResponseEntity.ok(artistMapper.toDto(artist));
    }

    /**
     * Creates a new artist in the system.
     *
     * @param req the incoming artist data
     * @return the newly created artist as ArtistDto with HTTP 201 status
     */
    @PostMapping
    public ResponseEntity<ArtistDto> createArtist(@RequestBody ArtistRequest req) {
        var entity = artistMapper.toEntity(req);
        var created = artistService.createArtist(entity);
        return ResponseEntity.status(201).body(artistMapper.toDto(created));
    }

    /**
     * Updates an existing artist's information.
     *
     * @param id  the ID of the artist to update
     * @param req the updated artist data
     * @return updated ArtistDto if successful
     */
    @PutMapping("/{id}")
    public ResponseEntity<ArtistDto> updateArtist(
            @PathVariable Long id,
            @RequestBody ArtistRequest req) {
        var updated = artistService.updateArtist(id, artistMapper.toEntity(req));
        return ResponseEntity.ok(artistMapper.toDto(updated));
    }

    /**
     * Retrieves all events associated with a given artist.
     *
     * @param id the artist's ID
     * @return list of events for the artist
     */
    @GetMapping("/{id}/events")
    public ResponseEntity<List<Event>> getEventsForArtist(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.listAllEventsForArtist(id));
    }

    /**
     * Retrieves the total number of tickets sold for an artist's performances.
     *
     * @param artistId the artist's ID
     * @return total ticket count for the artist
     */
    @GetMapping("/{artistId}/ticket-count")
    public ResponseEntity<Long> getTicketCountForArtist(@PathVariable Long artistId) {
        return ResponseEntity.ok(artistService.getTicketCountForArtist(artistId));
    }

    /**
     * Retrieves a list of all venues where the artist has performed or is scheduled to perform.
     *
     * @param artistId the artist's ID
     * @return list of venues for the artist
     */
    @GetMapping("/{artistId}/venues")
    public ResponseEntity<List<Venue>> getVenuesForArtist(@PathVariable Long artistId) {
        return ResponseEntity.ok(artistService.getVenuesForArtist(artistId));
    }

    /**
     * Deletes an artist by ID. Returns 204 No Content if deleted,
     * or 404 Not Found if the artist does not exist.
     *
     * @param id the artist's ID
     * @return empty 204 response or 404 if not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArtist(@PathVariable Long id) {
        try {
            artistService.deleteArtist(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
