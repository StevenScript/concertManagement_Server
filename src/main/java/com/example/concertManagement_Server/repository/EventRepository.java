package com.example.concertManagement_Server.repository;

import com.example.concertManagement_Server.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    // Retrieves all events occurring after a specified date
    List<Event> findByEventDateAfter(LocalDate date);

    // Retrieves all events associated with a specific artist by artist ID
    List<Event> findByArtists_Id(Long artistId);

    // Custom query to find events by a specific artist ID
    @Query("SELECT e FROM Event e JOIN e.artists a WHERE a.id = :artistId")
    List<Event> findEventsByArtistId(Long artistId);

}
