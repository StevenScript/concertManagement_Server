package com.example.concertManagement_Server.service;

import com.example.concertManagement_Server.dto.VenueRequest;
import com.example.concertManagement_Server.exception.ResourceNotFoundException;
import com.example.concertManagement_Server.model.Event;
import com.example.concertManagement_Server.model.Venue;
import com.example.concertManagement_Server.repository.EventRepository;
import com.example.concertManagement_Server.repository.VenueRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VenueService {

    private final VenueRepository venueRepository;
    private final EventRepository eventRepository;

    public VenueService(VenueRepository venueRepository, EventRepository eventRepository) {
        this.venueRepository = venueRepository;
        this.eventRepository = eventRepository;
    }

    /**
     * Retrieves a venue by ID, or throws if not found
     */
    public Venue getVenueById(Long id) {
        return venueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venue with id " + id + " not found"));
    }

    /**
     * Retrieves all venues from the database
     */
    public List<Venue> listAllVenues() {
        return venueRepository.findAll();
    }

    /**
     * Creates a new venue from a VenueRequest DTO
     */
    public Venue createVenue(VenueRequest req) {
        Venue venue = new Venue();
        venue.setName(req.getName());
        venue.setLocation(req.getLocation());
        venue.setCapacity(req.getCapacity());
        return venueRepository.save(venue);
    }

    /**
     * Updates an existing venue with data from a VenueRequest DTO
     */
    public Venue updateVenue(Long id, VenueRequest req) {
        return venueRepository.findById(id).map(venue -> {
            venue.setName(req.getName());
            venue.setLocation(req.getLocation());
            venue.setCapacity(req.getCapacity());
            return venueRepository.save(venue);
        }).orElseThrow(() -> new ResourceNotFoundException("Venue with id " + id + " not found"));
    }

    /**
     * Finds upcoming events for a specific venue by filtering on event date
     */
    public List<Event> findUpcomingEventsForVenue(Long venueId) {
        List<Event> allEventsForVenue = eventRepository.findByVenueId(venueId);
        LocalDate today = LocalDate.now();
        return allEventsForVenue.stream()
                .filter(e -> e.getEventDate().isAfter(today))
                .collect(Collectors.toList());
    }
}
