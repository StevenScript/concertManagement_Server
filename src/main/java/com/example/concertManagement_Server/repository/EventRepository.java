package com.example.concertManagement_Server.repository;

import com.example.concertManagement_Server.model.Event;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * CRUD + custom queries for Event.
 */
@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    /* ---------------- basic filters ---------------- */

    /** All events after a given date (unsorted). */
    List<Event> findByEventDateAfter(LocalDate date);

    /** Upcoming events ascending by date. */
    List<Event> findByEventDateAfterOrderByEventDateAsc(LocalDate today);

    /** Events belonging to an artist. */
    List<Event> findByArtists_Id(Long artistId);

    /** Same via explicit JPQL join. */
    @Query("SELECT e FROM Event e JOIN e.artists a WHERE a.id = :artistId")
    List<Event> findEventsByArtistId(Long artistId);

    /** Events hosted at a venue. */
    List<Event> findByVenueId(Long venueId);


    /* ---------------- landing-page stats ---------------- */

    /**
     * Top events by ticket sales.
     * Uses a native query because JPQL doesn’t allow COUNT+ORDER+LIMIT easily.
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

    /** Newest events by ID (proxy for created-at). */
    List<Event> findByOrderByIdDesc(Pageable pageable);

    /** Events within the next 7 days. */
    @Query("""
         SELECT e FROM Event e
         WHERE e.eventDate BETWEEN :start AND :end
         ORDER BY e.eventDate ASC
         """)
    List<Event> findUpcoming(LocalDate start, LocalDate end);
}
