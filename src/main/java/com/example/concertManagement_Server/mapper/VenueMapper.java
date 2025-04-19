package com.example.concertManagement_Server.mapper;

import com.example.concertManagement_Server.dto.VenueDto;
import com.example.concertManagement_Server.dto.VenueRequest;
import com.example.concertManagement_Server.model.Venue;
import org.springframework.stereotype.Component;

/**
 * Maps between Venue entity and its DTO/request representations.
 */
@Component
public class VenueMapper {

    /**
     * Converts a Venue entity to its DTO.
     *
     * @param venue the source Venue entity
     * @return a new VenueDto containing venue details
     */
    public VenueDto toDto(Venue venue) {
        return new VenueDto(
                venue.getId(),
                venue.getName(),
                venue.getLocation(),
                venue.getCapacity()
        );
    }

    /**
     * Creates a new Venue entity from a request DTO.
     *
     * @param req the VenueRequest with input data
     * @return a fresh Venue entity populated with request values
     */
    public Venue toEntity(VenueRequest req) {
        Venue venue = new Venue();
        venue.setName(req.getName());
        venue.setLocation(req.getLocation());
        venue.setCapacity(req.getCapacity());
        return venue;
    }

    /**
     * Updates an existing Venue entity’s fields from a request DTO.
     *
     * @param req   the VenueRequest with updated data
     * @param venue the existing Venue entity to modify
     */
    public void updateEntity(VenueRequest req, Venue venue) {
        venue.setName(req.getName());
        venue.setLocation(req.getLocation());
        venue.setCapacity(req.getCapacity());
    }
}