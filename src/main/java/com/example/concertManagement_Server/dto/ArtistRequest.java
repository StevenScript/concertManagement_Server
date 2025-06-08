/**
 * ArtistRequest.java
 *
 * Payload object used to create or update an artist via API.
 * This DTO represents the client's intent to modify artist data,
 * separate from internal models or exposed DTOs.
 *
 * Used by:
 * - ArtistController.java (for POST and PUT requests)
 * - ArtistService.java (input for entity creation or updates)
 */
package com.example.concertManagement_Server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArtistRequest {

    /**
     * The artist's public-facing stage or band name.
     */
    private String stageName;

    /**
     * The musical genre the artist performs.
     */
    private String genre;

    /**
     * Number of members in the group (e.g., 1 for solo artists).
     */
    private Integer membersCount;

    /**
     * The city or location the artist originates from.
     */
    private String homeCity;
}
