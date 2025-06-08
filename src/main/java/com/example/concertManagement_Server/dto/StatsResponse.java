/**
 * StatsResponse.java
 *
 * Aggregated payload containing high-value curated content for the animated landing page.
 * Groups events, artists, and venues into specific lists based on relevance or trend criteria.
 *
 * Used by:
 * - StatsController.java (GET /stats/landing)
 * - Frontend landing page (hero sections)
 */
package com.example.concertManagement_Server.dto;

import java.util.List;

public record StatsResponse(
        /**
         * Most popular or promoted events.
         */
        List<EventDto> topEvents,

        /**
         * Events scheduled in the near future.
         */
        List<EventDto> upcomingEvents,

        /**
         * Recently added or newly created events.
         */
        List<EventDto> newestEvents,

        /**
         * Artists with the highest engagement or ticket draws.
         */
        List<ArtistDto> topArtists,

        /**
         * Venues that are hosting multiple or popular events.
         */
        List<VenueDto> hotVenues
) {}
