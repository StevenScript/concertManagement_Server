/**
 * VenueRequest.java
 *
 * Payload used to create or update venue records.
 * Submitted through admin panels or backend setup tools.
 *
 * Used by:
 * - VenueController.java (POST and PUT endpoints)
 * - VenueService.java (create/update logic)
 */
package com.example.concertManagement_Server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VenueRequest {

    /**
     * Name of the venue.
     */
    private String name;

    /**
     * Address or general location of the venue.
     */
    private String location;

    /**
     * Maximum supported capacity for the venue.
     */
    private Integer capacity;
}
