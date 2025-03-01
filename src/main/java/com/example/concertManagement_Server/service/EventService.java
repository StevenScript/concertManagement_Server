package com.example.concertManagement_Server.service;

import com.example.concertManagement_Server.model.Artist;
import com.example.concertManagement_Server.model.Event;
import com.example.concertManagement_Server.repository.EventRepository;
import com.example.concertManagement_Server.repository.ArtistRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final ArtistRepository artistRepository;

    public EventService(EventRepository eventRepository, ArtistRepository artistRepository) {
        this.eventRepository = eventRepository;
        this.artistRepository = artistRepository;
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event getEventById(Long id) {
        Optional<Event> optional = eventRepository.findById(id);
        return optional.orElse(null);
    }

    public List<Event> findUpcomingEvents() {
        LocalDate today = LocalDate.now();
        return eventRepository.findByEventDateAfter(today);
    }

    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    public Event updateEvent(Long id, Event updatedData) {
        return eventRepository.findById(id).map(e -> {
            e.setEventDate(updatedData.getEventDate());
            e.setTicketPrice(updatedData.getTicketPrice());
            e.setAvailableTickets(updatedData.getAvailableTickets());
            e.setVenue(updatedData.getVenue());
            // etc.
            return eventRepository.save(e);
        }).orElse(null);
    }

    public List<Event> findEventsByArtistId(Long artistId) {
        return eventRepository.findByArtists_Id(artistId);
    }

    public Event addArtistToEvent(Long eventId, Long artistId) {
        Optional<Event> eventOpt = eventRepository.findById(eventId);
        // If the event doesn't exist, return null now, and never query the artist
        if (eventOpt.isEmpty()) {
            return null;
        }

        // Then check the artist
        Optional<Artist> artistOpt = artistRepository.findById(artistId);
        if (artistOpt.isEmpty()) {
            return null;
        }

        // Both event and artist exist
        Event event = eventOpt.get();
        Artist artist = artistOpt.get();
        event.getArtists().add(artist);
        return eventRepository.save(event);
    }

    public List<Event> listAllEventsForArtist(Long artistId) {
        return eventRepository.findEventsByArtistId(artistId);
    }
}
