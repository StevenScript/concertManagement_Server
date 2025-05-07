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

/**
 * Provides business operations for Venue entities,
 * including retrieval, creation, updates, and querying associated events.
 */
@Service
public class VenueService {

    private final VenueRepository venueRepository;
    private final EventRepository eventRepository;

    public VenueService(VenueRepository venueRepository,
                        EventRepository eventRepository) {
        this.venueRepository = venueRepository;
        this.eventRepository = eventRepository;
    }

    /**
     * Retrieves a venue by its unique identifier.
     *
     * @param id the ID of the venue
     * @return the Venue if found
     * @throws ResourceNotFoundException if no venue exists with the given ID
     */
    public Venue getVenueById(Long id) {
        return venueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Venue with id " + id + " not found"
                ));
    }

    /**
     * Returns all venues in the system.
     *
     * @return list of Venue entities
     */
    public List<Venue> listAllVenues() {
        return venueRepository.findAll();
    }

    /**
     * Creates and persists a new venue based on the provided DTO.
     *
     * @param req data for the new venue
     * @return the saved Venue entity
     */
    public Venue createVenue(VenueRequest req) {
        Venue venue = new Venue();
        venue.setName(req.getName());
        venue.setLocation(req.getLocation());
        venue.setCapacity(req.getCapacity());
        return venueRepository.save(venue);
    }

    /**
     * Updates an existing venue's details.
     *
     * @param id  the ID of the venue to update
     * @param req the new venue data
     * @return the updated Venue entity
     * @throws ResourceNotFoundException if the venue does not exist
     */
    public Venue updateVenue(Long id, VenueRequest req) {
        return venueRepository.findById(id)
                .map(venue -> {
                    venue.setName(req.getName());
                    venue.setLocation(req.getLocation());
                    venue.setCapacity(req.getCapacity());
                    return venueRepository.save(venue);
                })
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Venue with id " + id + " not found"
                ));
    }

    /**
     * Retrieves upcoming events for a given venue, filtered by date.
     *
     * @param venueId the ID of the venue
     * @return list of Event entities occurring after today
     */
    public List<Event> findUpcomingEventsForVenue(Long venueId) {
        List<Event> allEventsForVenue = eventRepository.findByVenueId(venueId);
        LocalDate today = LocalDate.now();
        return allEventsForVenue.stream()
                .filter(e -> e.getEventDate().isAfter(today))
                .collect(Collectors.toList());
    }

    public void deleteVenue(Long id) {
        if (!venueRepository.existsById(id)) {
            throw new ResourceNotFoundException("Venue id " + id + " not found");
        }
        venueRepository.deleteById(id);
    }
}
