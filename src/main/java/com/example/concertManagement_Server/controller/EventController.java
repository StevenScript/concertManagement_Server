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
     * Retrieves all events.
     */
    @GetMapping
    public List<EventDto> getAllEvents() {
        return eventService.getAllEvents().stream()
                .map(eventMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves upcoming events only.
     */
    @GetMapping("/upcoming")
    public List<EventDto> getUpcomingEvents() {
        return eventService.findUpcomingEvents().stream()
                .map(eventMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a specific event by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EventDto> getEvent(@PathVariable Long id) {
        Event event = eventService.getEventById(id);
        return ResponseEntity.ok(eventMapper.toDto(event));
    }

    /**
     * Creates a new event with associated venue and artists.
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
     * Updates an existing event.
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
     * Lists events for a given artist.
     */
    @GetMapping("/artist/{artistId}")
    public ResponseEntity<List<EventDto>> getEventsByArtist(@PathVariable Long artistId) {
        var dtos = eventService.listAllEventsForArtist(artistId).stream()
                .map(eventMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /**
     * Adds an artist to an existing event.
     */
    @PostMapping("/{eventId}/artists/{artistId}")
    public ResponseEntity<EventDto> addArtistToEvent(
            @PathVariable Long eventId,
            @PathVariable Long artistId) {
        Event updated = eventService.addArtistToEvent(eventId, artistId);
        return ResponseEntity.ok(eventMapper.toDto(updated));
    }

    /**
     * Retrieves all tickets for a specific event.
     */
    @GetMapping("/{id}/tickets")
    public ResponseEntity<List<Ticket>> getTicketsForEvent(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getTicketsForEvent(id));
    }

    /**
     * Retrieves ticket count for a specific event.
     */
    @GetMapping("/{id}/ticket-count")
    public ResponseEntity<Long> getTicketCountForEvent(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getTicketCountForEvent(id));
    }
}
