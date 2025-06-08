/**
 * VenueDto.java
 *
 * DTO used to expose venue details in API responses.
 * Meant for frontend display or search results.
 *
 * Used by:
 * - VenueController.java (GET endpoints)
 * - Event and stats lookups involving venue data
 */
package com.example.concertManagement_Server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VenueDto {

    /**
     * Unique ID of the venue.
     */
    private Long id;

    /**
     * Public-facing name of the venue.
     */
    private String name;

    /**
     * Location or address of the venue.
     */
    private String location;

    /**
     * Maximum number of attendees the venue can hold.
     */
    private Integer capacity;
}
