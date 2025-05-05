package com.example.concertManagement_Server.controller;

import com.example.concertManagement_Server.dto.EventDto;
import com.example.concertManagement_Server.dto.EventRequest;
import com.example.concertManagement_Server.mapper.EventMapper;
import com.example.concertManagement_Server.model.Event;
import com.example.concertManagement_Server.model.Ticket;
import com.example.concertManagement_Server.model.Venue;
import com.example.concertManagement_Server.model.Artist;
import com.example.concertManagement_Server.service.EventService;
import com.example.concertManagement_Server.service.VenueService;
import com.example.concertManagement_Server.service.ArtistService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for EventController covering CRUD and association endpoints.
 */
@ExtendWith(MockitoExtension.class)
public class EventControllerTest {

    @Mock
    private EventService eventService;
    @Mock
    private VenueService venueService;
    @Mock
    private ArtistService artistService;
    @Mock
    private EventMapper eventMapper;

    @InjectMocks
    private EventController eventController;

    /** Ensures getEvent(id) returns HTTP 200 and the correct DTO when found. */
    @Test
    void testGetEventDtoFound() {
        // Given
        Event entity = new Event();
        entity.setId(99L);
        entity.setName("Test Event");
        entity.setEventDate(LocalDate.of(2025, 5, 10));
        given(eventService.getEventById(99L)).willReturn(entity);

        EventDto dto = new EventDto(
                99L,
                "Test Event",
                entity.getEventDate(),
                null,
                null,
                null,
                null
        );
        given(eventMapper.toDto(entity)).willReturn(dto);

        // When
        ResponseEntity<EventDto> resp = eventController.getEvent(99L);

        // Then
        assertEquals(200, resp.getStatusCodeValue());
        assertEquals(dto, resp.getBody());
    }

    /** Confirms missing event throws ResourceNotFoundException. */
    @Test
    void testGetEventDtoNotFound() {
        given(eventService.getEventById(999L))
                .willThrow(new com.example.concertManagement_Server.exception.ResourceNotFoundException(
                        "Event with id 999 not found"));

        assertThrows(
                com.example.concertManagement_Server.exception.ResourceNotFoundException.class,
                () -> eventController.getEvent(999L));
    }

    /** Verifies createEvent pipeline: mapping → saving → mapping back → HTTP 201. */
    @Test
    void testCreateEventDtoPipeline() {
        // Given
        EventRequest req = new EventRequest(
                "Summer Show",
                LocalDate.of(2025, 6, 1),
                50.0,
                100,
                5L,
                Set.of(10L, 11L)
        );

        Venue venue = new Venue();
        venue.setId(5L);

        Artist a1 = new Artist();
        a1.setId(10L);
        Artist a2 = new Artist();
        a2.setId(11L);

        given(venueService.getVenueById(5L)).willReturn(venue);
        given(artistService.getArtistById(10L)).willReturn(a1);
        given(artistService.getArtistById(11L)).willReturn(a2);

        Event toSave = new Event();
        toSave.setName("Summer Show");
        given(eventMapper.toEntity(req, venue, Set.of(a1, a2))).willReturn(toSave);

        Event saved = new Event();
        saved.setId(20L);
        saved.setName("Summer Show");
        given(eventService.createEvent(toSave)).willReturn(saved);

        EventDto dto = new EventDto(
                20L,
                "Summer Show",
                null,
                null,
                null,
                null,
                null
        );
        given(eventMapper.toDto(saved)).willReturn(dto);

        // When
        ResponseEntity<EventDto> resp = eventController.createEvent(req);

        // Then
        assertEquals(201, resp.getStatusCodeValue());
        assertEquals(dto, resp.getBody());
    }

    /** Checks both tickets list and count endpoints return correct data. */
    @Test
    void testGetTicketsAndCount() {
        // Tickets
        Ticket t = new Ticket();
        t.setId(501L);
        given(eventService.getTicketsForEvent(2L)).willReturn(List.of(t));

        ResponseEntity<List<Ticket>> tr = eventController.getTicketsForEvent(2L);
        assertEquals(200, tr.getStatusCodeValue());
        assertEquals(501L, tr.getBody().get(0).getId());

        // Count
        given(eventService.getTicketCountForEvent(2L)).willReturn(150L);
        ResponseEntity<Long> cr = eventController.getTicketCountForEvent(2L);
        assertEquals(200, cr.getStatusCodeValue());
        assertEquals(150L, cr.getBody());
    }

    /** Verifies getAll and getUpcoming return mapped DTO lists. */
    @Test
    void testGetAllAndUpcomingEventsDtoPipeline() {
        Event event = new Event();
        event.setId(1L);
        event.setName("Any");
        given(eventService.getAllEvents()).willReturn(List.of(event));

        EventDto d = new EventDto(
                1L,
                "Any",
                null,
                null,
                null,
                null,
                null
        );
        given(eventMapper.toDto(event)).willReturn(d);

        List<EventDto> all = eventController.getAllEvents();
        assertEquals(1, all.size());
        assertEquals(d, all.get(0));

        given(eventService.findUpcomingEvents()).willReturn(List.of(event));
        List<EventDto> up = eventController.getUpcomingEvents();
        assertEquals(1, up.size());
        assertEquals(d, up.get(0));
    }

    /** Tests listing events by artist and adding an artist to an event. */
    @Test
    void testGetEventsByArtistAndAddArtistPipeline() {
        Event event = new Event();
        event.setId(2L);
        event.setName("Any2");

        EventDto d = new EventDto(
                2L,
                "Any2",
                null,
                null,
                null,
                null,
                null
        );

        given(eventService.listAllEventsForArtist(7L)).willReturn(List.of(event));
        given(eventMapper.toDto(event)).willReturn(d);

        ResponseEntity<List<EventDto>> lr = eventController.getEventsByArtist(7L);
        assertEquals(200, lr.getStatusCodeValue());
        assertEquals(1, lr.getBody().size());
        assertEquals(d, lr.getBody().get(0));

        given(eventService.addArtistToEvent(3L, 8L)).willReturn(event);
        given(eventMapper.toDto(event)).willReturn(d);

        ResponseEntity<EventDto> ar = eventController.addArtistToEvent(3L, 8L);
        assertEquals(200, ar.getStatusCodeValue());
        assertEquals(d, ar.getBody());
    }
}
