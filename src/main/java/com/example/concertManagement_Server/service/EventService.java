package com.example.concertManagement_Server.service;

import com.example.concertManagement_Server.exception.CapacityExceededException;
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
 * Encapsulates business-logic for managing events,
 * including CRUD, associations and ticket queries.
 */
@Service
public class EventService {

    private final EventRepository  eventRepository;
    private final ArtistRepository artistRepository;
    private final TicketRepository ticketRepository;

    public EventService(EventRepository  eventRepository,
                        ArtistRepository artistRepository,
                        TicketRepository ticketRepository) {
        this.eventRepository  = eventRepository;
        this.artistRepository = artistRepository;
        this.ticketRepository = ticketRepository;
    }

    /* ---------- helpers ---------- */

    /** Guard: availableTickets must not exceed venue capacity. */
    private void assertWithinCapacity(Event ev) {
        int capacity = ev.getVenue().getCapacity();
        if (ev.getAvailableTickets() > capacity) {
            throw new CapacityExceededException(
                    "Available tickets (" + ev.getAvailableTickets() +
                            ") exceed venue capacity (" + capacity + ')');
        }
    }

    /* ---------- CRUD ---------- */

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Event with id " + id + " not found"));
    }

    public List<Event> findUpcomingEvents() {
        return eventRepository
                .findByEventDateAfterOrderByEventDateAsc(LocalDate.now());
    }

    public Event createEvent(Event event) {
        assertWithinCapacity(event);
        return eventRepository.save(event);
    }

    public Event updateEvent(Long id, Event updatedData) {
        return eventRepository.findById(id)
                .map(e -> {
                    e.setName(updatedData.getName());
                    e.setEventDate(updatedData.getEventDate());
                    e.setTicketPrice(updatedData.getTicketPrice());
                    e.setAvailableTickets(updatedData.getAvailableTickets());
                    e.setVenue(updatedData.getVenue());
                    assertWithinCapacity(e);           // ← NEW guard
                    return eventRepository.save(e);
                })
                .orElseThrow(() ->
                        new ResourceNotFoundException("Event with id " + id + " not found"));
    }

    public void deleteEvent(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new ResourceNotFoundException("Event id " + id + " not found");
        }
        eventRepository.deleteById(id);
    }

    /* ---------- associations & look-ups ---------- */

    public List<Event> listAllEventsForArtist(Long artistId) {
        return eventRepository.findEventsByArtistId(artistId);
    }

    public Event addArtistToEvent(Long eventId, Long artistId) {
        Event  event  = getEventById(eventId);
        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Artist with id " + artistId + " not found"));

        event.getArtists().add(artist);
        return eventRepository.save(event);
    }

    /* ---------- tickets ---------- */

    /** Remaining tickets (pool – sold). */
    public Long getTicketsLeftForEvent(Long eventId) {
        Event ev   = getEventById(eventId);
        long sold  = ticketRepository.countByEventId(eventId);
        return ev.getAvailableTickets() - sold;
    }

    public List<Ticket> getTicketsForEvent(Long eventId) {
        return ticketRepository.findByEventId(eventId);
    }

    public Long getTicketCountForEvent(Long eventId) {
        return ticketRepository.countByEventId(eventId);
    }
}


