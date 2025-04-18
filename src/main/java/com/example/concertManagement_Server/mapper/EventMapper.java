package com.example.concertManagement_Server.mapper;

import com.example.concertManagement_Server.dto.EventDto;
import com.example.concertManagement_Server.dto.EventRequest;
import com.example.concertManagement_Server.model.Event;
import com.example.concertManagement_Server.model.Venue;
import com.example.concertManagement_Server.model.Artist;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class EventMapper {

    public EventDto toDto(Event e) {
        return new EventDto(
                e.getId(),
                e.getEventDate(),
                e.getTicketPrice(),
                e.getAvailableTickets(),
                e.getVenue() != null ? e.getVenue().getId() : null,
                e.getArtists() != null ? e.getArtists().stream()
                        .map(Artist::getId)
                        .collect(Collectors.toSet())
                        : null
        );
    }

    public Event toEntity(EventRequest req, Venue venue, Set<Artist> artists) {
        Event e = new Event();
        e.setEventDate(req.getEventDate());
        e.setTicketPrice(req.getTicketPrice());
        e.setAvailableTickets(req.getAvailableTickets());
        e.setVenue(venue);
        e.setArtists(artists);
        return e;
    }

    public void updateEntity(EventRequest req, Event e, Venue venue, Set<Artist> artists) {
        e.setEventDate(req.getEventDate());
        e.setTicketPrice(req.getTicketPrice());
        e.setAvailableTickets(req.getAvailableTickets());
        e.setVenue(venue);
        e.setArtists(artists);
    }
}
