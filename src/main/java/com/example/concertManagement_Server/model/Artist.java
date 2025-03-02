package com.example.concertManagement_Server.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "artists")
@Data // Lombok - generates getters, setters, toString, equals, hashCode
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "events")
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Primary key for the artist

    private String stageName;
    private String genre;
    private Integer membersCount;
    private String homeCity;

    @ManyToMany(mappedBy = "artists")
    @JsonIgnoreProperties("artists") // Prevents circular references in JSON responses
    private Set<Event> events = new HashSet<>();
}
