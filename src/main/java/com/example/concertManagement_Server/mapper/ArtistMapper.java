package com.example.concertManagement_Server.mapper;

import com.example.concertManagement_Server.dto.ArtistDto;
import com.example.concertManagement_Server.dto.ArtistRequest;
import com.example.concertManagement_Server.model.Artist;
import org.springframework.stereotype.Component;

@Component
public class ArtistMapper {

    public ArtistDto toDto(Artist a) {
        return new ArtistDto(
                a.getId(),
                a.getStageName(),
                a.getGenre(),
                a.getMembersCount(),
                a.getHomeCity()
        );
    }

    public Artist toEntity(ArtistRequest req) {
        Artist a = new Artist();
        a.setStageName(req.getStageName());
        a.setGenre(req.getGenre());
        a.setMembersCount(req.getMembersCount());
        a.setHomeCity(req.getHomeCity());
        return a;
    }

    public void updateEntity(ArtistRequest req, Artist a) {
        a.setStageName(req.getStageName());
        a.setGenre(req.getGenre());
        a.setMembersCount(req.getMembersCount());
        a.setHomeCity(req.getHomeCity());
    }
}
