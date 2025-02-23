package com.example.concertManagement_Server.repository;

import com.example.concertManagement_Server.model.Event;
import com.example.concertManagement_Server.model.Ticket;
import com.example.concertManagement_Server.model.Venue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

@DataJpaTest
public class TicketRepositoryTest {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private VenueRepository venueRepository;

    @Test
    void testSaveAndFindTicket() {
        // 1. Event to associate a Ticket with.
        //    That event, in turn, needs a Venue.
        Venue venue = new Venue();
        venue.setName("Test Venue");
        venue.setLocation("Test City");
        venue.setCapacity(1000);
        Venue savedVenue = venueRepository.save(venue);

        Event event = new Event();
        event.setEventDate(LocalDate.of(2025, 7, 20));
        event.setTicketPrice(100.0);
        event.setAvailableTickets(300);
        event.setVenue(savedVenue);
        Event savedEvent = eventRepository.save(event);

        // 2. Create a Ticket object referencing the event
        Ticket ticket = new Ticket();
        ticket.setEvent(savedEvent);
        ticket.setSeatNumber("A12");
        ticket.setTicketType("VIP");
        ticket.setBuyerName("John Doe");

        // 3. Save the ticket using the ticketRepository
        Ticket savedTicket = ticketRepository.save(ticket);

        // 4. Verify the ticket got an ID
        Assertions.assertNotNull(savedTicket.getId(), "Ticket should have an auto-generated ID");

        // 5. Retrieve by ID
        Ticket foundTicket = ticketRepository.findById(savedTicket.getId()).orElse(null);
        Assertions.assertNotNull(foundTicket, "The ticket should be found in the database");
        Assertions.assertEquals("A12", foundTicket.getSeatNumber());
        Assertions.assertEquals("VIP", foundTicket.getTicketType());
        Assertions.assertEquals("John Doe", foundTicket.getBuyerName());
        Assertions.assertEquals(savedEvent.getId(), foundTicket.getEvent().getId());
    }
}
