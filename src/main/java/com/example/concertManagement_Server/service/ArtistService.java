package com.example.concertManagement_Server.service;

import com.example.concertManagement_Server.model.Artist;
import com.example.concertManagement_Server.model.Venue;
import com.example.concertManagement_Server.model.Event;
import com.example.concertManagement_Server.repository.ArtistRepository;
import com.example.concertManagement_Server.repository.EventRepository;
import com.example.concertManagement_Server.repository.TicketRepository;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;

import java.util.List;
import java.util.Optional;

@Service
public class ArtistService {
    private final ArtistRepository artistRepository;
    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;

    // Constructor injection for repository dependency
    public ArtistService(ArtistRepository artistRepository,
                         EventRepository eventRepository,
                         TicketRepository ticketRepository) {
        this.artistRepository = artistRepository;
        this.eventRepository = eventRepository;
        this.ticketRepository = ticketRepository;
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

    public Long getTicketCountForArtist(Long artistId) {
        // Retrieve all events for the given artist using a custom repository method
        List<Event> events = eventRepository.findEventsByArtistId(artistId);

        long totalTickets = 0;
        // For each event, count the tickets using the ticket repository
        for (Event event : events) {
            totalTickets += ticketRepository.countByEventId(event.getId());
        }

        return totalTickets;
    }

    public List<Venue> getVenuesForArtist(Long artistId) {
        // Retrieve all events where the artist is performing using a repository method
        List<Event> events = eventRepository.findEventsByArtistId(artistId);

        // Extract the venues from these events, filter to get distinct entries
        return events.stream()
                .map(Event::getVenue)
                .filter(venue -> venue != null)
                .distinct()
                .collect(Collectors.toList());
    }


    // Retrieves all artists associated with a specific venue
    public List<Artist> listAllArtistsForVenue(Long venueId) {
        return artistRepository.findArtistsByVenueId(venueId);
    }
}