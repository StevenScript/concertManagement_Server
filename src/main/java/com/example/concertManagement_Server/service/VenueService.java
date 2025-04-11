package com.example.concertManagement_Server.service;

import com.example.concertManagement_Server.model.Event;
import com.example.concertManagement_Server.model.Venue;
import com.example.concertManagement_Server.repository.EventRepository;
import com.example.concertManagement_Server.repository.VenueRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VenueService {

    private final VenueRepository venueRepository;
    private final EventRepository eventRepository;

    // Constructor injection for both repositories
    public VenueService(VenueRepository venueRepository, EventRepository eventRepository) {
        this.venueRepository = venueRepository;
        this.eventRepository = eventRepository;
    }

    // Retrieves a venue by ID, or returns null if not found
    public Venue getVenueById(Long id) {
        Optional<Venue> optional = venueRepository.findById(id);
        return optional.orElse(null);
    }

    // Retrieves all venues from the database
    public List<Venue> listAllVenues() {
        return venueRepository.findAll();
    }

    // Creates and saves a new venue
    public Venue createVenue(Venue venue) {
        return venueRepository.save(venue);
    }

    // Updates an existing venue if found
    public Venue updateVenue(Long id, Venue updatedData) {
        return venueRepository.findById(id).map(venue -> {
            venue.setName(updatedData.getName());
            venue.setLocation(updatedData.getLocation());
            venue.setCapacity(updatedData.getCapacity());
            return venueRepository.save(venue);
        }).orElse(null);
    }

    public List<Event> findUpcomingEventsForVenue(Long venueId) {
        // Retrieve all events for this venue using a custom repository query or filtering
        List<Event> allEventsForVenue = eventRepository.findByVenueId(venueId);
        LocalDate today = LocalDate.now();
        return allEventsForVenue.stream()
                .filter(e -> e.getEventDate().isAfter(today))
                .collect(Collectors.toList());
    }
}
