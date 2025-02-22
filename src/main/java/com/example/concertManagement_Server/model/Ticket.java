package com.example.concertManagement_Server.model;


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
    private Long id;

    // Many tickets -> one event
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    private String seatNumber;
    private String ticketType; // VIP, GA, etc.
    private String buyerName;
}
