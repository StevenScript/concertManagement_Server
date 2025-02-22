package com.example.concertManagement_Server.repository;

import com.example.concertManagement_Server.model.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {
}
