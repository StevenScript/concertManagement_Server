package com.example.concertManagement_Server.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a musical artist or band, including their genre, group size, and origin city.
 * Artists can perform at multiple events (Many-to-Many).
 */
@Entity
@Table(name = "artists")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "events")
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The stage name or performance name. */
    @Column(nullable = false)
    private String stageName;

    /** The music genre this artist is known for. */
    private String genre;

    /** Number of members (e.g., solo = 1, band = 4, etc.). */
    private Integer membersCount;

    /** The city this artist is based in or represents. */
    private String homeCity;

    /** Events this artist is scheduled to perform in. */
    @ManyToMany(mappedBy = "artists")
    @JsonIgnoreProperties("artists")
    private Set<Event> events = new HashSet<>();
}
