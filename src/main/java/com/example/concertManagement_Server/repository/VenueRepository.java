package com.example.concertManagement_Server.repository;

import com.example.concertManagement_Server.model.Venue;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * CRUD + “hottest” venues.
 */
@Repository
public interface VenueRepository extends JpaRepository<Venue, Long> {

    /**
     * Venues hosting the most events (descending).
     * Supply a Pageable for the LIMIT, e.g. PageRequest.of(0, limit).
     */
    @Query("""
         SELECT v FROM Venue v
         JOIN v.events e
         GROUP BY v
         ORDER BY COUNT(e.id) DESC
         """)
    List<Venue> findHottestVenues(Pageable pageable);
}
