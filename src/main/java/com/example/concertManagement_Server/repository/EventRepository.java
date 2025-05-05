package com.example.concertManagement_Server.repository;

import com.example.concertManagement_Server.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository for Event entities, offering CRUD operations
 * and queries for date- and artist-based filtering.
 */
@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    /**
     * Retrieves events occurring strictly after the specified date.
     *
     * @param date the cutoff date
     * @return list of upcoming events
     */
    List<Event> findByEventDateAfter(LocalDate date);

    /**
     * Retrieves events associated with a specific artist by their ID.
     *
     * @param artistId the ID of the artist
     * @return list of events featuring that artist
     */
    List<Event> findByArtists_Id(Long artistId);

    /**
     * Finds events for a given artist using a JPQL join.
     *
     * @param artistId the ID of the artist
     * @return list of events featuring that artist
     */
    @Query("SELECT e FROM Event e JOIN e.artists a WHERE a.id = :artistId")
    List<Event> findEventsByArtistId(Long artistId);

    /**
     * Retrieves all events hosted at a specific venue.
     *
     * @param venueId the ID of the venue
     * @return list of events at that venue
     */
    List<Event> findByVenueId(Long venueId);


    List<Event> findByEventDateAfterOrderByEventDateAsc(LocalDate today);
}