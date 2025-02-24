package com.example.concertManagement_Server.service;

import com.example.concertManagement_Server.model.Artist;
import com.example.concertManagement_Server.model.Event;
import com.example.concertManagement_Server.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event getEventById(Long id) {
        Optional<Event> optional = eventRepository.findById(id);
        return optional.orElse(null);
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

    public Event addArtistToEvent(Long eventId, Artist artist) {
        return eventRepository.findById(eventId).map(e -> {
            e.getArtists().add(artist); // add to the set
            return eventRepository.save(e);
        }).orElse(null);
    }

    public List<Event> listAllEventsForArtist(Long artistId) {
        return eventRepository.findEventsByArtistId(artistId);
    }
}
