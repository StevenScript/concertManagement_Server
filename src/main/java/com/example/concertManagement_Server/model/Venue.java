package com.example.concertManagement_Server.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a venue where events are hosted.
 */
@Entity
@Table(name = "venues")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Venue {

    /**
     * Unique identifier for the venue.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Name of the venue.
     */
    private String name;

    /**
     * City where the venue is located.
     */
    private String location;

    /**
     * Maximum audience capacity of the venue.
     */
    private Integer capacity;

    /**
     * Events scheduled at this venue.
     */
    @OneToMany(mappedBy = "venue")
    @JsonIgnoreProperties("venue")
    private List<Event> events = new ArrayList<>();
}