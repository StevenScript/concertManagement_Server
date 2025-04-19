package com.example.concertManagement_Server.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a musical artist with their details and associated events.
 */
@Entity
@Table(name = "artists")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "events")
public class Artist {

    /**
     * Unique identifier for the artist.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Stage name of the artist.
     */
    private String stageName;

    /**
     * Musical genre performed by the artist.
     */
    private String genre;

    /**
     * Number of members in the artist's group.
     */
    private Integer membersCount;

    /**
     * Home city of the artist.
     */
    private String homeCity;

    /**
     * Events where the artist is performing.
     */
    @ManyToMany(mappedBy = "artists")
    @JsonIgnoreProperties("artists")
    private Set<Event> events = new HashSet<>();
}