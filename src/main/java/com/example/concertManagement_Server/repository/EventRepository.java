package com.example.concertManagement_Server.repository;

import com.example.concertManagement_Server.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("SELECT e FROM Event e JOIN e.artists a WHERE a.id = :artistId")
    List<Event> findEventsByArtistId(Long artistId);
}
