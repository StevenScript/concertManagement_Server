package com.example.concertManagement_Server.service;

import com.example.concertManagement_Server.exception.ResourceNotFoundException;
import com.example.concertManagement_Server.model.Artist;
import com.example.concertManagement_Server.model.Event;
import com.example.concertManagement_Server.repository.EventRepository;
import com.example.concertManagement_Server.repository.ArtistRepository;
import com.example.concertManagement_Server.model.Ticket;
import com.example.concertManagement_Server.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final ArtistRepository artistRepository;
    private final TicketRepository ticketRepository;

    // Retrieves all events from the database
    public EventService(EventRepository eventRepository, ArtistRepository artistRepository, TicketRepository ticketRepository) {
        this.eventRepository = eventRepository;
        this.artistRepository = artistRepository;
        this.ticketRepository = ticketRepository;
    }

    // Retrieves an event by ID, or returns null if not found
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public List<Ticket> getTicketsForEvent(Long eventId) {
        return ticketRepository.findByEventId(eventId);
    }

    public Long getTicketCountForEvent(Long eventId) {
        return ticketRepository.countByEventId(eventId);
    }

    // Retrieves upcoming events based on the current date
    public Event getEventById(Long id) {
        Optional<Event> optional = eventRepository.findById(id);
        return optional.orElseThrow(() -> new ResourceNotFoundException("Artist with id " + id + " not found"));
    }

    // Creates and saves a new event
    public List<Event> findUpcomingEvents() {
        LocalDate today = LocalDate.now();
        return eventRepository.findByEventDateAfter(today);
    }

    // Updates an existing event if found
    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    // Retrieves all events associated with a specific artist
    public Event updateEvent(Long id, Event updatedData) {
        return eventRepository.findById(id).map(e -> {
            e.setEventDate(updatedData.getEventDate());
            e.setTicketPrice(updatedData.getTicketPrice());
            e.setAvailableTickets(updatedData.getAvailableTickets());
            e.setVenue(updatedData.getVenue());
            // etc.
            return eventRepository.save(e);
        }).orElseThrow(() -> new ResourceNotFoundException("Artist with id " + id + " not found"));
    }

    // Adds an artist to an event if both exist
    public List<Event> findEventsByArtistId(Long artistId) {
        return eventRepository.findByArtists_Id(artistId);
    }

    public Event addArtistToEvent(Long eventId, Long artistId) {
        // Attempt to retrieve the event; if not found, throw an exception
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event with id " + eventId + " not found"));
        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new ResourceNotFoundException("Artist with id " + artistId + " not found"));

        // Add the artist to the event and save the event
        event.getArtists().add(artist);
        return eventRepository.save(event);
    }

    // Retrieves all events for a specific artist
    public List<Event> listAllEventsForArtist(Long artistId) {
        return eventRepository.findEventsByArtistId(artistId);
    }
}
