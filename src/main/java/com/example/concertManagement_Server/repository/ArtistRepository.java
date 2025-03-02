package com.example.concertManagement_Server.repository;

import com.example.concertManagement_Server.model.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {

    // Custom query to find artists performing at a specific venue
    @Query("SELECT distinct a FROM Artist a JOIN a.events e WHERE e.venue.id = :venueId")
    List<Artist> findArtistsByVenueId(Long venueId);
}
