package com.example.concertManagement_Server.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

/**
 * Represents a ticket purchased for an event.
 */
@Entity
@Table(name = "tickets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {

    /**
     * Unique identifier for the ticket.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Event associated with this ticket.
     */
    @ManyToOne
    @JsonIgnoreProperties("tickets")
    @JoinColumn(name = "event_id")
    private Event event;

    /**
     * Seat number assigned to the ticket.
     */
    private String seatNumber;

    /**
     * Type of ticket (e.g., VIP, GA).
     */
    private String ticketType;

    /**
     * Name of the ticket buyer.
     */
    private String buyerName;
}
