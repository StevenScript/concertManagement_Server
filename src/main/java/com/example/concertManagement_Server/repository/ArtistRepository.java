/**
 * ArtistRepository.java
 *
 * Repository interface for performing CRUD operations on Artist entities,
 * as well as additional custom queries for:
 * - Retrieving artists booked at a specific venue
 * - Finding "top" artists tied to specific event IDs
 *
 * Works closely with:
 * - Artist.java (entity model)
 * - ArtistService.java (business logic layer)
 * - EventRepository.java (cross-query via events)
 */
package com.example.concertManagement_Server.repository;

import com.example.concertManagement_Server.model.Artist;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {

    /**
     * Finds all artists who are booked at a given venue.
     *
     * @param venueId the ID of the venue
     * @return list of artists performing at the venue
     */
    @Query("""
         SELECT DISTINCT a FROM Artist a
         JOIN a.events e
         WHERE e.venue.id = :venueId
         """)
    List<Artist> findArtistsByVenueId(Long venueId);

    /**
     * Retrieves a list of artists tied to specific event IDs,
     * ordered by relevance for landing-page display.
     *
     * @param eventIds the list of relevant event IDs
     * @param pageable a Pageable object (use PageRequest.of(0, limit))
     * @return list of artists for display
     */
    @Query("""
         SELECT DISTINCT a FROM Artist a
         JOIN a.events e
         WHERE e.id IN :eventIds
         """)
    List<Artist> findTopArtists(List<Long> eventIds, Pageable pageable);
}
