package com.example.concertManagement_Server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request payload for creating or updating an Artist.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArtistRequest {
    private String stageName;
    private String genre;
    private Integer membersCount;
    private String homeCity;
}
