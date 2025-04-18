package com.example.concertManagement_Server.controller;

import com.example.concertManagement_Server.dto.ArtistDto;
import com.example.concertManagement_Server.dto.ArtistRequest;
import com.example.concertManagement_Server.mapper.ArtistMapper;
import com.example.concertManagement_Server.model.Event;
import com.example.concertManagement_Server.model.Venue;
import com.example.concertManagement_Server.service.ArtistService;
import com.example.concertManagement_Server.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/artists")
public class ArtistController {

    private final ArtistService artistService;
    private final EventService eventService;
    private final ArtistMapper artistMapper;

    public ArtistController(ArtistService artistService,
                            EventService eventService,
                            ArtistMapper artistMapper) {
        this.artistService = artistService;
        this.eventService = eventService;
        this.artistMapper = artistMapper;
    }

    @GetMapping
    public List<ArtistDto> getAllArtists() {
        return artistService.getAllArtists().stream()
                .map(artistMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArtistDto> getArtist(@PathVariable Long id) {
        var a = artistService.getArtistById(id);
        return ResponseEntity.ok(artistMapper.toDto(a));
    }

    @PostMapping
    public ResponseEntity<ArtistDto> createArtist(@RequestBody ArtistRequest req) {
        var entity = artistMapper.toEntity(req);
        var created = artistService.createArtist(entity);
        return new ResponseEntity<>(artistMapper.toDto(created), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArtistDto> updateArtist(@PathVariable Long id,
                                                  @RequestBody ArtistRequest req) {
        var updated = artistService.updateArtist(id, artistMapper.toEntity(req));
        return ResponseEntity.ok(artistMapper.toDto(updated));
    }

    @GetMapping("/{id}/events")
    public ResponseEntity<List<Event>> getEventsForArtist(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.listAllEventsForArtist(id));
    }

    @GetMapping("/{artistId}/ticket-count")
    public ResponseEntity<Long> getTicketCountForArtist(@PathVariable Long artistId) {
        return ResponseEntity.ok(artistService.getTicketCountForArtist(artistId));
    }

    @GetMapping("/{artistId}/venues")
    public ResponseEntity<List<Venue>> getVenuesForArtist(@PathVariable Long artistId) {
        return ResponseEntity.ok(artistService.getVenuesForArtist(artistId));
    }
}
