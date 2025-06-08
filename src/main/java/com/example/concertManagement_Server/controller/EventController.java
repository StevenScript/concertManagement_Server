/**
 * EventController.java
 *
 * REST controller responsible for managing concert events.
 * Provides endpoints for:
 * - Full CRUD on events
 * - Querying events by artist
 * - Associating artists with events
 * - Fetching ticket data (count, availability, etc.)
 *
 * Works closely with:
 * - EventService.java (core event logic)
 * - VenueService.java (event-venue relationship)
 * - ArtistService.java (event-artist relationship)
 * - EventMapper.java (DTO <-> entity conversion)
 * - EventRequest / EventDto (API models)
 */
package com.example.concertManagement_Server.controller;

import com.example.concertManagement_Server.dto.EventDto;
import com.example.concertManagement_Server.dto.EventRequest;
import com.example.concertManagement_Server.mapper.EventMapper;
import com.example.concertManagement_Server.model.Artist;
import com.example.concertManagement_Server.model.Event;
import com.example.concertManagement_Server.model.Ticket;
import com.example.concertManagement_Server.model.Venue;
import com.example.concertManagement_Server.service.ArtistService;
import com.example.concertManagement_Server.service.EventService;
import com.example.concertManagement_Server.service.VenueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;
    private final VenueService venueService;
    private final ArtistService artistService;
    private final EventMapper eventMapper;

    public EventController(
            EventService eventService,
            VenueService venueService,
            ArtistService artistService,
            EventMapper eventMapper) {
        this.eventService = eventService;
        this.venueService = venueService;
        this.artistService = artistService;
        this.eventMapper = eventMapper;
    }

    /**
     * Retrieves all events in the system.
     *
     * @return list of all EventDto objects
     */
    @GetMapping
    public List<EventDto> getAllEvents() {
        return eventService.getAllEvents().stream()
                .map(eventMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves only upcoming events (scheduled after current time).
     *
     * @return list of upcoming EventDto objects
     */
    @GetMapping("/upcoming")
    public List<EventDto> getUpcomingEvents() {
        return eventService.findUpcomingEvents().stream()
                .map(eventMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a specific event by its ID.
     *
     * @param id the event ID
     * @return the event as EventDto
     */
    @GetMapping("/{id}")
    public ResponseEntity<EventDto> getEvent(@PathVariable Long id) {
        Event event = eventService.getEventById(id);
        return ResponseEntity.ok(eventMapper.toDto(event));
    }

    /**
     * Creates a new event and links it to a venue and artists.
     *
     * @param req the event creation request payload
     * @return the created event as EventDto
     */
    @PostMapping
    public ResponseEntity<EventDto> createEvent(@RequestBody EventRequest req) {
        Venue venue = venueService.getVenueById(req.getVenueId());
        Set<Artist> artists = req.getArtistIds().stream()
                .map(artistService::getArtistById)
                .collect(Collectors.toSet());

        Event entity = eventMapper.toEntity(req, venue, artists);
        Event saved = eventService.createEvent(entity);
        return ResponseEntity.status(201).body(eventMapper.toDto(saved));
    }

    /**
     * Updates an existing event’s data.
     *
     * @param id  the event ID
     * @param req the new event data
     * @return the updated event as EventDto
     */
    @PutMapping("/{id}")
    public ResponseEntity<EventDto> updateEvent(
            @PathVariable Long id,
            @RequestBody EventRequest req) {
        Venue venue = venueService.getVenueById(req.getVenueId());
        Set<Artist> artists = req.getArtistIds().stream()
                .map(artistService::getArtistById)
                .collect(Collectors.toSet());

        Event existing = eventService.getEventById(id);
        eventMapper.updateEntity(req, existing, venue, artists);
        Event updated = eventService.updateEvent(id, existing);
        return ResponseEntity.ok(eventMapper.toDto(updated));
    }

    /**
     * Retrieves all events an artist is performing at.
     *
     * @param artistId the artist’s ID
     * @return list of EventDto for that artist
     */
    @GetMapping("/artist/{artistId}")
    public ResponseEntity<List<EventDto>> getEventsByArtist(@PathVariable Long artistId) {
        var dtos = eventService.listAllEventsForArtist(artistId).stream()
                .map(eventMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /**
     * Adds a new artist to an existing event.
     *
     * @param eventId  the event ID
     * @param artistId the artist ID to add
     * @return updated event as EventDto
     */
    @PostMapping("/{eventId}/artists/{artistId}")
    public ResponseEntity<EventDto> addArtistToEvent(
            @PathVariable Long eventId,
            @PathVariable Long artistId) {
        Event updated = eventService.addArtistToEvent(eventId, artistId);
        return ResponseEntity.ok(eventMapper.toDto(updated));
    }

    /**
     * Retrieves all tickets associated with a specific event.
     *
     * @param id the event ID
     * @return list of Ticket objects
     */
    @GetMapping("/{id}/tickets")
    public ResponseEntity<List<Ticket>> getTicketsForEvent(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getTicketsForEvent(id));
    }

    /**
     * Retrieves how many tickets are still available for an event.
     *
     * @param id the event ID
     * @return number of remaining tickets
     */
    @GetMapping("/{id}/tickets-left")
    public ResponseEntity<Long> getTicketsLeft(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getTicketsLeftForEvent(id));
    }

    /**
     * Retrieves the total number of tickets originally created for the event.
     *
     * @param id the event ID
     * @return total ticket count
     */
    @GetMapping("/{id}/ticket-count")
    public ResponseEntity<Long> getTicketCountForEvent(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getTicketCountForEvent(id));
    }

    /**
     * Deletes an event by its ID.
     *
     * @param id the event ID
     * @return 204 No Content on successful deletion
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
}

