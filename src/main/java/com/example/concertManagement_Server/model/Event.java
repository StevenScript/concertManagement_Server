package com.example.concertManagement_Server.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a concert or performance event, with pricing, artists, and associated venue.
 * Events have many artists, and belong to one venue.
 */
@Entity
@Table(name = "events")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "artists")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Display name of the event (e.g. “Summer Jam 2025”). */
    @Column(nullable = false)
    private String name;

    /** Date on which the event takes place. */
    private LocalDate eventDate;

    /** Cost of one ticket (in dollars or chosen currency). */
    private Double ticketPrice;

    /** Maximum number of tickets available for purchase. */
    private Integer availableTickets;

    /** Venue where this event is scheduled. */
    @ManyToOne
    @JoinColumn(name = "venue_id")
    @JsonIgnoreProperties("events")
    private Venue venue;

    /** Artists performing at this event. */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "event_artists",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "artist_id")
    )
    @JsonIgnoreProperties("events")
    private Set<Artist> artists = new HashSet<>();
}
