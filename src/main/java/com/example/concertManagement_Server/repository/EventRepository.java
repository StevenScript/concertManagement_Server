/**
 * EventRepository.java
 *
 * Repository interface for performing CRUD operations on Event entities,
 * including filtering and reporting capabilities such as:
 * - Date-based and venue-based event queries
 * - Events by artist
 * - Statistical lookups for landing-page components (top-selling, newest, upcoming)
 *
 * Works closely with:
 * - Event.java (entity model)
 * - EventService.java (business logic layer)
 * - TicketRepository.java (for sales stats)
 * - ArtistRepository.java (event-artist relationships)
 */
package com.example.concertManagement_Server.repository;

import com.example.concertManagement_Server.model.Event;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    // ----- Basic filters -----

    /**
     * Finds all events occurring after a specified date.
     *
     * @param date the cutoff date
     * @return list of future events
     */
    List<Event> findByEventDateAfter(LocalDate date);

    /**
     * Finds all upcoming events ordered chronologically.
     *
     * @param today the current date
     * @return sorted list of future events
     */
    List<Event> findByEventDateAfterOrderByEventDateAsc(LocalDate today);

    /**
     * Retrieves all events associated with a given artist by artist ID.
     *
     * @param artistId the ID of the artist
     * @return list of events for that artist
     */
    List<Event> findByArtists_Id(Long artistId);

    /**
     * Alternative JPQL-based query for retrieving artist events.
     *
     * @param artistId the ID of the artist
     * @return list of events the artist is linked to
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


    // ----- Landing-page stats -----

    /**
     * Finds top events based on ticket sales.
     * Native SQL is used for performance and flexibility.
     *
     * @param limit number of top events to return
     * @return list of top-selling events
     */
    @Query(
            value = """
              SELECT e.*
              FROM events e
              LEFT JOIN tickets t ON t.event_id = e.id
              GROUP BY e.id
              ORDER BY COUNT(t.id) DESC
              LIMIT :limit
              """,
            nativeQuery = true)
    List<Event> findTopEvents(int limit);

    /**
     * Finds the most recently created events based on descending ID.
     *
     * @param pageable page config with size limit
     * @return list of newest events
     */
    List<Event> findByOrderByIdDesc(Pageable pageable);

    /**
     * Retrieves events occurring within a specific date range (e.g., next 7 days).
     *
     * @param start start of range
     * @param end   end of range
     * @return list of events in range, ordered by date
     */
    @Query("""
         SELECT e FROM Event e
         WHERE e.eventDate BETWEEN :start AND :end
         ORDER BY e.eventDate ASC
         """)
    List<Event> findUpcoming(LocalDate start, LocalDate end);
}
