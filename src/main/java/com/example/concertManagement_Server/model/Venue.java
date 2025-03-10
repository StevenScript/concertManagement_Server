package com.example.concertManagement_Server.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "venues")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Venue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Primary key for the venue

    private String name;
    private String location; // City where the venue is located
    private Integer capacity;

    // One venue can have multiple events
    @OneToMany(mappedBy = "venue")
    @JsonIgnoreProperties("venue") // Prevent infinite recursion when serializing events
    private List<Event> events = new ArrayList<>();
}
