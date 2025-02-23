package com.example.concertManagement_Server.controller;

import com.example.concertManagement_Server.model.Artist;
import com.example.concertManagement_Server.service.ArtistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/artists")
public class ArtistController {

    private final ArtistService artistService;

    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Artist> getArtist(@PathVariable Long id) {
        Artist artist = artistService.getArtistById(id);
        if (artist == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(artist);
    }

    @PostMapping
    public ResponseEntity<Artist> createArtist(@RequestBody Artist artist) {
        Artist created = artistService.createArtist(artist);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
}
