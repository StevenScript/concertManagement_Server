package com.example.concertManagement_Server.model;

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
    private Long id;

    private String name;
    private String location; // Planning on City/Province
    private Integer capacity;

    // One venue has many events
    @OneToMany(mappedBy = "venue")
    private List<Event> events = new ArrayList<>();
}
