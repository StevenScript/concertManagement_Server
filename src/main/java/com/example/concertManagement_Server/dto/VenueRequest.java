package com.example.concertManagement_Server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Payload for creating or updating a venue.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VenueRequest {
    private String name;
    private String location;
    private Integer capacity;
}