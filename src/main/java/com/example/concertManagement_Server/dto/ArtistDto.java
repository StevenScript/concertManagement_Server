package com.example.concertManagement_Server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * View object for Artist, exposing only safe fields.
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

