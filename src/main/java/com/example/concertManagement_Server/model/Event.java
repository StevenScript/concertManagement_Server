package com.example.concertManagement_Server.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents an event, including date, pricing, venue, and participating artists.
 */
@Entity
@Table(name = "events")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "artists")
public class Event {

    /**
     * Unique identifier for the event.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Date of the event.
     */
    private LocalDate eventDate;

    /**
     * Ticket price for the event.
     */
    private Double ticketPrice;

    /**
     * Number of tickets available for sale.
     */
    private Integer availableTickets;

    /**
     * Venue where the event takes place.
     */
    @ManyToOne
    @JoinColumn(name = "venue_id")
    @JsonIgnoreProperties("events")
    private Venue venue;

    /**
     * Artists participating in the event.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "event_artists",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "artist_id")
    )
    @JsonIgnoreProperties("events")
    private Set<Artist> artists = new HashSet<>();
}
