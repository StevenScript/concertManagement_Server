package com.example.concertManagement_Server.repository;

import com.example.concertManagement_Server.model.Event;
import com.example.concertManagement_Server.model.Ticket;
import com.example.concertManagement_Server.model.Venue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

@DataJpaTest
@ActiveProfiles("test")
public class TicketRepositoryTest {

    @Autowired private TicketRepository ticketRepository;
    @Autowired private EventRepository  eventRepository;
    @Autowired private VenueRepository  venueRepository;

    @Test
    void testSaveAndFindTicket() {
        Venue venue = new Venue();
        venue.setName("Test Venue");
        venue.setLocation("Test City");
        venue.setCapacity(1000);
        Venue savedVenue = venueRepository.save(venue);

        Event event = new Event();
        event.setName("TicketEvent");
        event.setEventDate(LocalDate.of(2025, 7, 20));
        event.setTicketPrice(100.0);
        event.setAvailableTickets(300);
        event.setVenue(savedVenue);
        Event savedEvent = eventRepository.save(event);

        Ticket ticket = new Ticket();
        ticket.setEvent(savedEvent);
        ticket.setBuyerEmail("john@example.com");

        Ticket savedTicket = ticketRepository.save(ticket);
        Assertions.assertNotNull(savedTicket.getId());

        Ticket foundTicket = ticketRepository.findById(savedTicket.getId()).orElse(null);
        Assertions.assertNotNull(foundTicket);
        Assertions.assertEquals("john@example.com", foundTicket.getBuyerEmail());
        Assertions.assertEquals(savedEvent.getId(), foundTicket.getEvent().getId());
    }
}