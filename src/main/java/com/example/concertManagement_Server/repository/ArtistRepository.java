package com.example.concertManagement_Server.repository;

import com.example.concertManagement_Server.model.Artist;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * CRUD + venue / stats helpers for Artist.
 */
@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {

    /** Artists booked at a given venue. */
    @Query("""
         SELECT DISTINCT a FROM Artist a
         JOIN a.events e
         WHERE e.venue.id = :venueId
         """)
    List<Artist> findArtistsByVenueId(Long venueId);

    /**
     * “Top” artists tied to a set of event-IDs (used for landing page).
     * Pass a Pageable like PageRequest.of(0, limit).
     */
    @Query("""
         SELECT DISTINCT a FROM Artist a
         JOIN a.events e
         WHERE e.id IN :eventIds
         """)
    List<Artist> findTopArtists(List<Long> eventIds, Pageable pageable);
}
