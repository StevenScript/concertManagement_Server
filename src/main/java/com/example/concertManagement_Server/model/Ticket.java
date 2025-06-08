package com.example.concertManagement_Server.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

/**
 * Represents a single ticket issued for an event.
 */
@Entity
@Table(name = "tickets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Event this ticket belongs to. */
    @ManyToOne
    @JoinColumn(name = "event_id")
    @JsonIgnoreProperties("tickets")
    private Event event;

    /** Optional seat number (e.g., A12, GA, etc.). */
    private String seatNumber;

    /** Ticket type or class (e.g., VIP, General Admission). */
    private String ticketType;

    /** Email of the buyer (nullable if user is authenticated). */
    private String buyerEmail;
}
