/**
 * ArtistDto.java
 *
 * A Data Transfer Object (DTO) used to expose artist data to clients without revealing internal model structure.
 * Typically used in API responses to safely represent artist information.
 *
 * Used by:
 * - ArtistController.java
 * - Event-related queries that return artist data
 * - Venue-based lookups of booked artists
 */
package com.example.concertManagement_Server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArtistDto {

    /**
     * Unique ID of the artist.
     */
    private Long id;

    /**
     * The artist's public-facing stage or band name.
     */
    private String stageName;

    /**
     * The musical genre the artist primarily performs.
     */
    private String genre;

    /**
     * Number of members in the group (1 for solo acts).
     */
    private Integer membersCount;

    /**
     * The home city or origin of the artist.
     */
    private String homeCity;
}


