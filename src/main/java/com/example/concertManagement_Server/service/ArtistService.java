package com.example.concertManagement_Server.service;

import com.example.concertManagement_Server.model.Artist;
import com.example.concertManagement_Server.repository.ArtistRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArtistService {
    private final ArtistRepository artistRepository;

    // Constructor injection for repository dependency
    public ArtistService(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    // Retrieves all artists from the database
    public List<Artist> getAllArtists() {
        return artistRepository.findAll();
    }

    // Retrieves an artist by ID, or returns null if not found
    public Artist getArtistById(Long id) {
        Optional<Artist> optionalArtist = artistRepository.findById(id);
        return optionalArtist.orElse(null);
    }

    // Creates and saves a new artist record
    public Artist createArtist(Artist artist) {
        // Potential business logic, e.g., validate fields
        return artistRepository.save(artist);
    }

    // Updates an existing artist's details if found
    public Artist updateArtist(Long id, Artist updatedData) {
        return artistRepository.findById(id).map(artist -> {
            // We found an Artist, apply changes
            artist.setStageName(updatedData.getStageName());
            artist.setGenre(updatedData.getGenre());
            artist.setHomeCity(updatedData.getHomeCity());

            return artistRepository.save(artist);
        }).orElse(null);
    }

    // Retrieves all artists associated with a specific venue
    public List<Artist> listAllArtistsForVenue(Long venueId) {
        return artistRepository.findArtistsByVenueId(venueId);
    }
}