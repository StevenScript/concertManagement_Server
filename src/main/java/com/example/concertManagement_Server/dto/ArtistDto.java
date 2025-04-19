package com.example.concertManagement_Server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for exposing artist data to clients.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArtistDto {
    private Long id;
    private String stageName;
    private String genre;
    private Integer membersCount;
    private String homeCity;
}

