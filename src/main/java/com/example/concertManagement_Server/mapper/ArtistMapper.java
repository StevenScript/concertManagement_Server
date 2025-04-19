package com.example.concertManagement_Server.mapper;

import com.example.concertManagement_Server.dto.ArtistDto;
import com.example.concertManagement_Server.dto.ArtistRequest;
import com.example.concertManagement_Server.model.Artist;
import org.springframework.stereotype.Component;

/**
 * Maps between Artist entity and its DTO/request representations.
 */
@Component
public class ArtistMapper {

    /**
     * Converts an Artist entity to its DTO.
     *
     * @param artist the source Artist entity
     * @return a new ArtistDto containing the artist’s data
     */
    public ArtistDto toDto(Artist artist) {
        return new ArtistDto(
                artist.getId(),
                artist.getStageName(),
                artist.getGenre(),
                artist.getMembersCount(),
                artist.getHomeCity()
        );
    }

    /**
     * Creates a new Artist entity from a request DTO.
     *
     * @param req the incoming ArtistRequest containing new artist data
     * @return a fresh Artist entity populated with request values
     */
    public Artist toEntity(ArtistRequest req) {
        Artist artist = new Artist();
        artist.setStageName(req.getStageName());
        artist.setGenre(req.getGenre());
        artist.setMembersCount(req.getMembersCount());
        artist.setHomeCity(req.getHomeCity());
        return artist;
    }

    /**
     * Updates an existing Artist entity’s fields from a request DTO.
     *
     * @param req    the ArtistRequest holding updated values
     * @param artist the existing Artist entity to be modified
     */
    public void updateEntity(ArtistRequest req, Artist artist) {
        artist.setStageName(req.getStageName());
        artist.setGenre(req.getGenre());
        artist.setMembersCount(req.getMembersCount());
        artist.setHomeCity(req.getHomeCity());
    }
}