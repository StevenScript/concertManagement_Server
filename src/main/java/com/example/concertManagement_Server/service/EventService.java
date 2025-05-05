package com.example.concertManagement_Server.service;

import com.example.concertManagement_Server.exception.ResourceNotFoundException;
import com.example.concertManagement_Server.model.Artist;
import com.example.concertManagement_Server.model.Event;
import com.example.concertManagement_Server.model.Ticket;
import com.example.concertManagement_Server.repository.ArtistRepository;
import com.example.concertManagement_Server.repository.EventRepository;
import com.example.concertManagement_Server.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Encapsulates business logic for managing events,
 * including CRUD operations, associations, and ticket queries.
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
     * Returns all events.
     */
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    /**
     * Retrieves an event by its ID.
     *
     * @throws ResourceNotFoundException if not found
     */
    public Event getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Event with id " + id + " not found")
                );
    }

    /**
     * Finds events occurring after today.
     */
    public List<Event> findUpcomingEvents() {
        return eventRepository
                .findByEventDateAfterOrderByEventDateAsc(LocalDate.now());
    }

    /**
     * Persists a new event.
     */
    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    /**
     * Updates an existing event's fields.
     *
     * @throws ResourceNotFoundException if not found
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
                        new ResourceNotFoundException("Event with id " + id + " not found")
                );
    }

    /**
     * Lists all events for the given artist.
     * Delegates to the repository method that your test stubs.
     */
    public List<Event> listAllEventsForArtist(Long artistId) {
        return eventRepository.findEventsByArtistId(artistId);
    }

    /**
     * Adds an existing artist to an existing event.
     *
     * @throws ResourceNotFoundException if event or artist not found
     */
    public Event addArtistToEvent(Long eventId, Long artistId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Event with id " + eventId + " not found")
                );
        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Artist with id " + artistId + " not found")
                );

        event.getArtists().add(artist);
        return eventRepository.save(event);
    }

    /**
     * Retrieves all tickets for a specific event.
     */
    public List<Ticket> getTicketsForEvent(Long eventId) {
        return ticketRepository.findByEventId(eventId);
    }

    /**
     * Counts tickets sold for a specific event.
     */
    public Long getTicketCountForEvent(Long eventId) {
        return ticketRepository.countByEventId(eventId);
    }
}

