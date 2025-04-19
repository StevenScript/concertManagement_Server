package com.example.concertManagement_Server.repository;

import com.example.concertManagement_Server.model.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repository for Artist entities, providing CRUD operations
 * and a custom query to find artists by venue.
 */
@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {

    /**
     * Finds distinct artists who are scheduled to perform at the given venue.
     *
     * @param venueId the ID of the venue
     * @return list of artists performing at that venue
     */
    @Query("SELECT DISTINCT a FROM Artist a JOIN a.events e WHERE e.venue.id = :venueId")
    List<Artist> findArtistsByVenueId(Long venueId);
}