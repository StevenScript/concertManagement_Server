package com.example.concertManagement_Server.controller;

import com.example.concertManagement_Server.model.Artist;
import com.example.concertManagement_Server.model.Event;
import com.example.concertManagement_Server.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    // Constructor-based dependency injection for the event service
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    // Retrieves all events from the database
    @GetMapping
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

    // Retrieves a list of upcoming events based on the current date
    @GetMapping("/upcoming")  // ✅ Correct endpoint
    public List<Event> getUpcomingEvents() {
        return eventService.findUpcomingEvents();
    }

    // Retrieves an event by its ID
    @GetMapping("/{id}")
    public ResponseEntity<Event> getEvent(@PathVariable Long id) {
        Event event = eventService.getEventById(id);
        if (event == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(event);
    }

    // Creates a new event entry
    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        Event created = eventService.createEvent(event);
        return ResponseEntity.status(201).body(created);
    }

    // Updates an existing event by ID
    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @RequestBody Event eventData) {
        Event updated = eventService.updateEvent(id, eventData);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    // Retrieves all events associated with a specific artist by artist ID
    @GetMapping("/artist/{artistId}")
    public ResponseEntity<List<Event>> getEventsByArtistId(@PathVariable Long artistId) {
        List<Event> events = eventService.listAllEventsForArtist(artistId);
        if (events.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(events);
    }

    // Adds an artist to an existing event
    @PostMapping("/{eventId}/artists/{artistId}")
    public ResponseEntity<Event> addArtistToEvent(@PathVariable Long eventId, @PathVariable Long artistId) {
        Event updatedEvent = eventService.addArtistToEvent(eventId, artistId);
        return updatedEvent != null ? ResponseEntity.ok(updatedEvent) : ResponseEntity.notFound().build();
    }
}
