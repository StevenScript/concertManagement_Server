package com.example.concertManagement_Server.repository;

import com.example.concertManagement_Server.model.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Venue entities, offering standard
 * CRUD operations on venue records.
 */
@Repository
public interface VenueRepository extends JpaRepository<Venue, Long> {
    // No custom methods able to be implemented in time
}