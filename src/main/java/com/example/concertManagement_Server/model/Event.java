package com.example.concertManagement_Server.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "events")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate eventDate;
    private Double ticketPrice;
    private Integer availableTickets;

    // Many events -> one venue
    @ManyToOne
    @JoinColumn(name = "venue_id")
    private Venue venue;

    // Many-to-many with Artist
    @ManyToMany
    @JoinTable(
            name = "event_artists",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "artist_id")
    )
    private Set<Artist> artists = new HashSet<>();
}
