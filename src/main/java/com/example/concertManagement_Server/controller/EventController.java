package com.example.concertManagement_Server.controller;

import com.example.concertManagement_Server.dto.EventDto;
import com.example.concertManagement_Server.dto.EventRequest;
import com.example.concertManagement_Server.mapper.EventMapper;
import com.example.concertManagement_Server.model.Event;
import com.example.concertManagement_Server.model.Ticket;
import com.example.concertManagement_Server.model.Venue;
import com.example.concertManagement_Server.model.Artist;
import com.example.concertManagement_Server.service.EventService;
import com.example.concertManagement_Server.service.VenueService;
import com.example.concertManagement_Server.service.ArtistService;
import org.springframework.http.HttpStatus;
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

    public EventController(EventService eventService,
                           VenueService venueService,
                           ArtistService artistService,
                           EventMapper eventMapper) {
        this.eventService = eventService;
        this.venueService = venueService;
        this.artistService = artistService;
        this.eventMapper = eventMapper;
    }

    @GetMapping
    public List<EventDto> getAllEvents() {
        return eventService.getAllEvents().stream()
                .map(eventMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/upcoming")
    public List<EventDto> getUpcomingEvents() {
        return eventService.findUpcomingEvents().stream()
                .map(eventMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDto> getEvent(@PathVariable Long id) {
        Event e = eventService.getEventById(id);
        return ResponseEntity.ok(eventMapper.toDto(e));
    }

    @PostMapping
    public ResponseEntity<EventDto> createEvent(@RequestBody EventRequest req) {
        Venue v = venueService.getVenueById(req.getVenueId());
        Set<Artist> artists = req.getArtistIds().stream()
                .map(artistService::getArtistById)
                .collect(Collectors.toSet());
        Event entity = eventMapper.toEntity(req, v, artists);
        Event saved = eventService.createEvent(entity);
        return new ResponseEntity<>(eventMapper.toDto(saved), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventDto> updateEvent(@PathVariable Long id, @RequestBody EventRequest req) {
        Venue v = venueService.getVenueById(req.getVenueId());
        Set<Artist> artists = req.getArtistIds().stream()
                .map(artistService::getArtistById)
                .collect(Collectors.toSet());
        Event existing = eventService.getEventById(id);
        eventMapper.updateEntity(req, existing, v, artists);
        Event saved = eventService.updateEvent(id, existing);
        return ResponseEntity.ok(eventMapper.toDto(saved));
    }

    @GetMapping("/artist/{artistId}")
    public ResponseEntity<List<EventDto>> getEventsByArtist(@PathVariable Long artistId) {
        List<EventDto> dtos = eventService.listAllEventsForArtist(artistId).stream()
                .map(eventMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/{eventId}/artists/{artistId}")
    public ResponseEntity<EventDto> addArtistToEvent(@PathVariable Long eventId, @PathVariable Long artistId) {
        Event updated = eventService.addArtistToEvent(eventId, artistId);
        return ResponseEntity.ok(eventMapper.toDto(updated));
    }

    @GetMapping("/{id}/tickets")
    public ResponseEntity<List<Ticket>> getTicketsForEvent(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getTicketsForEvent(id));
    }

    @GetMapping("/{id}/ticket-count")
    public ResponseEntity<Long> getTicketCountForEvent(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getTicketCountForEvent(id));
    }
}
