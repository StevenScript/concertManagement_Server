package com.example.concertManagement_Server.service;

import com.example.concertManagement_Server.model.Artist;
import com.example.concertManagement_Server.repository.ArtistRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ArtistService {
    private final ArtistRepository artistRepository;

    // Constructor injection
    public ArtistService(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    public Artist getArtistById(Long id) {
        Optional<Artist> optionalArtist = artistRepository.findById(id);
        return optionalArtist.orElse(null);
    }

    public Artist createArtist(Artist artist) {
        // Potential business logic, e.g., validate fields
        return artistRepository.save(artist);
    }
}