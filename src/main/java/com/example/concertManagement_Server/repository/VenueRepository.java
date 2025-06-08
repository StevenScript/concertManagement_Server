/**
 * VenueRepository.java
 *
 * Repository for Venue entities, supporting basic CRUD and
 * analytics-style queries for identifying popular venues.
 *
 * Works closely with:
 * - Venue.java (entity model)
 * - EventRepository.java (for venue-event relationships)
 * - VenueService.java (business logic for venue queries)
 */
package com.example.concertManagement_Server.repository;

import com.example.concertManagement_Server.model.Venue;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VenueRepository extends JpaRepository<Venue, Long> {

    /**
     * Retrieves venues ordered by the number of events hosted, descending.
     * Provide a Pageable (e.g., PageRequest.of(0, limit)) to cap results.
     *
     * @param pageable pagination and size info
     * @return list of most active venues
     */
    @Query("""
         SELECT v FROM Venue v
         JOIN v.events e
         GROUP BY v
         ORDER BY COUNT(e.id) DESC
         """)
    List<Venue> findHottestVenues(Pageable pageable);
}
