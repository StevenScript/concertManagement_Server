package com.example.concertManagement_Server.mapper;

import com.example.concertManagement_Server.dto.EventDto;
import com.example.concertManagement_Server.dto.EventRequest;
import com.example.concertManagement_Server.model.Event;
import com.example.concertManagement_Server.model.Venue;
import com.example.concertManagement_Server.model.Artist;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Maps between Event entity and its DTO/request representations.
 */
@Component
public class EventMapper {

    /**
     * Converts an Event entity to its DTO.
     *
     * @param event the source Event entity
     * @return a new EventDto containing event details
     */
    public EventDto toDto(Event event) {
        return new EventDto(
                event.getId(),
                event.getEventDate(),
                event.getTicketPrice(),
                event.getAvailableTickets(),
                event.getVenue() != null ? event.getVenue().getId() : null,
                event.getArtists() != null
                        ? event.getArtists().stream()
                        .map(Artist::getId)
                        .collect(Collectors.toSet())
                        : Set.of()
        );
    }

    /**
     * Creates a new Event entity from a request DTO, venue, and artist set.
     *
     * @param req     the EventRequest with input data
     * @param venue   the Venue entity to associate
     * @param artists the set of Artist entities to associate
     * @return a fresh Event entity populated with provided values
     */
    public Event toEntity(EventRequest req, Venue venue, Set<Artist> artists) {
        Event event = new Event();
        event.setEventDate(req.getEventDate());
        event.setTicketPrice(req.getTicketPrice());
        event.setAvailableTickets(req.getAvailableTickets());
        event.setVenue(venue);
        event.setArtists(artists);
        return event;
    }

    /**
     * Updates an existing Event entity’s fields from a request DTO.
     *
     * @param req     the EventRequest with updated data
     * @param event   the existing Event entity to modify
     * @param venue   the Venue to set
     * @param artists the Artist set to set
     */
    public void updateEntity(EventRequest req,
                             Event event,
                             Venue venue,
                             Set<Artist> artists) {
        event.setEventDate(req.getEventDate());
        event.setTicketPrice(req.getTicketPrice());
        event.setAvailableTickets(req.getAvailableTickets());
        event.setVenue(venue);
        event.setArtists(artists);
    }
}
