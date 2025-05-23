package com.example.concertManagement_Server.service;

import com.example.concertManagement_Server.exception.ResourceNotFoundException;
import com.example.concertManagement_Server.model.Artist;
import com.example.concertManagement_Server.model.Event;
import com.example.concertManagement_Server.model.Venue;
import com.example.concertManagement_Server.repository.ArtistRepository;
import com.example.concertManagement_Server.repository.EventRepository;
import com.example.concertManagement_Server.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Provides business logic for managing artists, including retrieval,
 * creation, updates, and related ticket and venue information.
 */
@Service
public class ArtistService {

    private final ArtistRepository artistRepository;
    private final EventRepository  eventRepository;
    private final TicketRepository ticketRepository;

    public ArtistService(ArtistRepository artistRepository,
                         EventRepository eventRepository,
                         TicketRepository ticketRepository) {
        this.artistRepository = artistRepository;
        this.eventRepository  = eventRepository;
        this.ticketRepository = ticketRepository;
    }

    /* ---------- basic CRUD ---------- */

    public List<Artist> getAllArtists() {
        return artistRepository.findAll();
    }

    public Artist getArtistById(Long id) {
        return artistRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Artist with id " + id + " not found"));
    }

    public Artist createArtist(Artist artist) {
        return artistRepository.save(artist);
    }

    public Artist updateArtist(Long id, Artist updatedData) {
        return artistRepository.findById(id)
                .map(a -> {
                    a.setStageName(updatedData.getStageName());
                    a.setGenre(updatedData.getGenre());
                    a.setMembersCount(updatedData.getMembersCount());
                    a.setHomeCity(updatedData.getHomeCity());
                    return artistRepository.save(a);
                })
                .orElseThrow(() ->
                        new ResourceNotFoundException("Artist with id " + id + " not found"));
    }

    public void deleteArtist(Long id) {
        if (!artistRepository.existsById(id)) {
            throw new ResourceNotFoundException("Artist id " + id + " not found");
        }
        artistRepository.deleteById(id);
    }

    /* ---------- analytics ---------- */

    public Long getTicketCountForArtist(Long artistId) {
        List<Event> events = eventRepository.findEventsByArtistId(artistId);
        return events.stream()
                .mapToLong(e -> ticketRepository.countByEventId(e.getId()))
                .sum();
    }

    /**
     * Returns every unique venue where this artist has an event.
     * Uses event → venue relationship, so it works even without
     * a direct Artist-Venue mapping table.
     */
    public List<Venue> getVenuesForArtist(Long artistId) {
        return eventRepository.findEventsByArtistId(artistId).stream()
                .map(Event::getVenue)          // event → venue
                .filter(Objects::nonNull)      // guard against nulls
                .collect(Collectors.toMap(     // distinct by venue id
                        Venue::getId,
                        v -> v,
                        (v1, v2) -> v1         // keep first
                ))
                .values()
                .stream()
                .collect(Collectors.toList());
    }

    /* ---------- reverse lookup ---------- */

    public List<Artist> listAllArtistsForVenue(Long venueId) {
        return artistRepository.findArtistsByVenueId(venueId);
    }
}
