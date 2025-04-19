package com.example.concertManagement_Server.service;

import com.example.concertManagement_Server.exception.ResourceNotFoundException;
import com.example.concertManagement_Server.model.Artist;
import com.example.concertManagement_Server.model.Event;
import com.example.concertManagement_Server.model.Ticket;
import com.example.concertManagement_Server.repository.EventRepository;
import com.example.concertManagement_Server.repository.ArtistRepository;
import com.example.concertManagement_Server.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Encapsulates business operations for managing events,
 * including CRUD operations and artist associations.
 */
@Service
public class EventService {

    private final EventRepository eventRepository;
    private final ArtistRepository artistRepository;
    private final TicketRepository ticketRepository;

    public EventService(EventRepository eventRepository,
                        ArtistRepository artistRepository,
                        TicketRepository ticketRepository) {
        this.eventRepository = eventRepository;
        this.artistRepository = artistRepository;
        this.ticketRepository = ticketRepository;
    }

    /**
     * Retrieves all events from the data store.
     */
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    /**
     * Retrieves an event by its ID or throws if not found.
     */
    public Event getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Event with id " + id + " not found"
                        )
                );
    }

    /**
     * Finds upcoming events after the current date.
     */
    public List<Event> findUpcomingEvents() {
        LocalDate today = LocalDate.now();
        return eventRepository.findByEventDateAfter(today);
    }

    /**
     * Persists a new event.
     */
    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    /**
     * Updates fields of an existing event.
     */
    public Event updateEvent(Long id, Event updatedData) {
        return eventRepository.findById(id)
                .map(e -> {
                    e.setEventDate(updatedData.getEventDate());
                    e.setTicketPrice(updatedData.getTicketPrice());
                    e.setAvailableTickets(updatedData.getAvailableTickets());
                    e.setVenue(updatedData.getVenue());
                    return eventRepository.save(e);
                })
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Event with id " + id + " not found"
                        )
                );
    }

    /**
     * Retrieves all events associated with a specific artist.
     */
    public List<Event> listAllEventsForArtist(Long artistId) {
        return eventRepository.findByArtists_Id(artistId);
    }

    /**
     * Adds an artist to an existing event.
     */
    public Event addArtistToEvent(Long eventId, Long artistId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Event with id " + eventId + " not found"
                        )
                );
        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Artist with id " + artistId + " not found"
                        )
                );

        event.getArtists().add(artist);
        return eventRepository.save(event);
    }

    /**
     * Retrieves all tickets for a given event.
     */
    public List<Ticket> getTicketsForEvent(Long eventId) {
        return ticketRepository.findByEventId(eventId);
    }

    /**
     * Counts tickets sold for a given event.
     */
    public Long getTicketCountForEvent(Long eventId) {
        return ticketRepository.countByEventId(eventId);
    }
}
