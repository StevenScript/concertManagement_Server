package com.example.concertManagement_Server.service;

import com.example.concertManagement_Server.dto.*;
import com.example.concertManagement_Server.mapper.*;
import com.example.concertManagement_Server.model.Event;
import com.example.concertManagement_Server.repository.ArtistRepository;
import com.example.concertManagement_Server.repository.EventRepository;
import com.example.concertManagement_Server.repository.TicketRepository;
import com.example.concertManagement_Server.repository.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final EventRepository eventRepo;
    private final TicketRepository ticketRepo;
    private final ArtistRepository artistRepo;
    private final VenueRepository venueRepo;

    private final EventMapper  eventMapper;
    private final ArtistMapper artistMapper;
    private final VenueMapper  venueMapper;

    /** Data for the animated landing page. */
    @Transactional(readOnly = true)
    public StatsResponse getLandingStats(int limit) {

        /* ---------- top events by tickets sold ---------- */
        List<EventDto> topEvents = eventRepo.findAll().stream()
                .sorted((a, b) -> Long.compare(
                        ticketRepo.countByEventId(b.getId()),
                        ticketRepo.countByEventId(a.getId())
                ))
                .limit(limit)
                .map(eventMapper::toDto)
                .toList();

        /* ---------- upcoming events (next 7 days) ---------- */
        LocalDate today = LocalDate.now();
        LocalDate in7   = today.plusDays(7);
        List<EventDto> upcoming = eventRepo
                .findByEventDateAfterOrderByEventDateAsc(today).stream()
                .filter(e -> !e.getEventDate().isAfter(in7))
                .limit(limit)
                .map(eventMapper::toDto)
                .toList();

        /* ---------- newest events (recently added) ---------- */
        List<EventDto> newest = eventRepo.findAll().stream()
                .sorted(Comparator.comparing(Event::getId).reversed()) // id ~ insertion
                .limit(limit)
                .map(eventMapper::toDto)
                .toList();

        /* ---------- top artists (by tickets sold) ---------- */
        List<ArtistDto> topArtists = artistRepo.findAll().stream()
                .sorted((a, b) -> Long.compare(
                        sumSold(b.getId()), sumSold(a.getId())
                ))
                .limit(limit)
                .map(artistMapper::toDto)
                .toList();

        /* ---------- hottest venues (by events hosted) ---------- */
        List<VenueDto> hotVenues = venueRepo.findAll().stream()
                .sorted((v1, v2) ->
                        Integer.compare(
                                v2.getEvents().size(),
                                v1.getEvents().size()
                        ))
                .limit(limit)
                .map(venueMapper::toDto)
                .toList();

        return new StatsResponse(topEvents, upcoming, newest, topArtists, hotVenues);
    }

    /* helper */
    private long sumSold(Long artistId) {
        return eventRepo.findEventsByArtistId(artistId).stream()
                .mapToLong(e -> ticketRepo.countByEventId(e.getId()))
                .sum();
    }
}



