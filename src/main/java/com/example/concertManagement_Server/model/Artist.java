package com.example.concertManagement_Server.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "artists")
@Data // Lombok - generates getters, setters, toString, equals, hashCode
@NoArgsConstructor
@AllArgsConstructor
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String stageName;
    private String genre;
    private Integer membersCount;
    private String homeCity;

    @ManyToMany(mappedBy = "artists")
    private Set<Event> events = new HashSet<>();
}
