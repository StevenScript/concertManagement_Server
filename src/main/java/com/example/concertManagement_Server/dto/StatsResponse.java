package com.example.concertManagement_Server.dto;



import java.util.List;

/**
 * Payload containing the five “hero” lists for the landing page.
 */
public record StatsResponse(
        List<EventDto>   topEvents,
        List<EventDto>   upcomingEvents,
        List<EventDto>   newestEvents,
        List<ArtistDto>  topArtists,
        List<VenueDto>   hotVenues
) {}
