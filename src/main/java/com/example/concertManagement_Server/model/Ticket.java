package com.example.concertManagement_Server.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tickets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Primary key for the ticket

    // Many tickets -> one event
    @ManyToOne
    @JsonIgnoreProperties("tickets") // Prevents infinite recursion when serializing events
    @JoinColumn(name = "event_id")
    private Event event;
    private String seatNumber;
    private String ticketType; // VIP, GA, etc.
    private String buyerName;
}
