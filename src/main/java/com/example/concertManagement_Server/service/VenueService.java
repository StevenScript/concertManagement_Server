package com.example.concertManagement_Server.service;

import com.example.concertManagement_Server.model.Venue;
import com.example.concertManagement_Server.repository.VenueRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VenueService {

    private final VenueRepository venueRepository;

    public VenueService(VenueRepository venueRepository) {
        this.venueRepository = venueRepository;
    }

    public Venue getVenueById(Long id) {
        Optional<Venue> optional = venueRepository.findById(id);
        return optional.orElse(null);
    }

    public List<Venue> listAllVenues() {
        return venueRepository.findAll();
    }

    public Venue createVenue(Venue venue) {
        return venueRepository.save(venue);
    }

    public Venue updateVenue(Long id, Venue updatedData) {
        return venueRepository.findById(id).map(venue -> {
            venue.setName(updatedData.getName());
            venue.setLocation(updatedData.getLocation());
            venue.setCapacity(updatedData.getCapacity());
            return venueRepository.save(venue);
        }).orElse(null);
    }
}
