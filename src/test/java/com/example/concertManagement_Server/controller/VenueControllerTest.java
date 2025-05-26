package com.example.concertManagement_Server.controller;

import com.example.concertManagement_Server.dto.*;
import com.example.concertManagement_Server.mapper.*;
import com.example.concertManagement_Server.model.Artist;
import com.example.concertManagement_Server.model.Event;
import com.example.concertManagement_Server.model.Venue;
import com.example.concertManagement_Server.service.ArtistService;
import com.example.concertManagement_Server.service.VenueService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class VenueControllerTest {

    /* ---------- mocks ---------- */
    @Mock private VenueService  venueService;
    @Mock private ArtistService artistService;

    @Mock private VenueMapper  venueMapper;
    @Mock private ArtistMapper artistMapper;
    @Mock private EventMapper  eventMapper;

    @InjectMocks
    private VenueController venueController;

    /* ---------- venue CRUD tests ---------- */

    @Test
    void testGetVenue_Found() {
        Venue entity = new Venue(); entity.setId(100L); entity.setName("Test");
        VenueDto dto  = new VenueDto(100L, "Test", null, null);

        given(venueService.getVenueById(100L)).willReturn(entity);
        given(venueMapper.toDto(entity)).willReturn(dto);

        ResponseEntity<VenueDto> resp = venueController.getVenue(100L);
        assertEquals(200, resp.getStatusCodeValue());
        assertEquals(dto, resp.getBody());
    }

    @Test
    void testListAllVenues() {
        Venue v = new Venue(); v.setId(1L); v.setName("One");
        VenueDto d = new VenueDto(1L, "One", null, null);

        given(venueService.listAllVenues()).willReturn(List.of(v));
        given(venueMapper.toDto(v)).willReturn(d);

        ResponseEntity<List<VenueDto>> resp = venueController.listAllVenues();
        assertEquals(200, resp.getStatusCodeValue());
        assertEquals(List.of(d), resp.getBody());
    }

    @Test
    void testCreateVenue() {
        VenueRequest req = new VenueRequest("Name","Loc",123);
        Venue entity     = new Venue(); entity.setId(5L); entity.setName("Name");
        VenueDto dto     = new VenueDto(5L, "Name", "Loc", 123);

        given(venueService.createVenue(req)).willReturn(entity);
        given(venueMapper.toDto(entity)).willReturn(dto);

        ResponseEntity<VenueDto> resp = venueController.createVenue(req);
        assertEquals(201, resp.getStatusCodeValue());
        assertEquals(dto, resp.getBody());
    }

    @Test
    void testUpdateVenue() {
        VenueRequest req = new VenueRequest("Up","Loc2",200);
        Venue entity     = new Venue(); entity.setId(7L); entity.setName("Up");
        VenueDto dto     = new VenueDto(7L, "Up", "Loc2", 200);

        given(venueService.updateVenue(7L, req)).willReturn(entity);
        given(venueMapper.toDto(entity)).willReturn(dto);

        ResponseEntity<VenueDto> resp = venueController.updateVenue(7L, req);
        assertEquals(200, resp.getStatusCodeValue());
        assertEquals(dto, resp.getBody());
    }

    /* ---------- extra look-ups ---------- */

    @Test
    void testGetArtistsForVenue() {
        Artist entity = new Artist(); entity.setId(100L); entity.setStageName("The Testers");
        ArtistDto dto = new ArtistDto(100L, "The Testers", "Rock", 4, "NYC");

        given(artistService.listAllArtistsForVenue(5L)).willReturn(List.of(entity));
        given(artistMapper.toDto(entity)).willReturn(dto);

        ResponseEntity<List<ArtistDto>> resp = venueController.getArtistsForVenue(5L);
        assertEquals(200, resp.getStatusCodeValue());
        assertEquals(List.of(dto), resp.getBody());

        verify(artistService).listAllArtistsForVenue(5L);
    }

    @Test
    void testGetUpcomingEventsForVenue() {
        Long venueId = 1L;

        Event entity = new Event();
        entity.setId(101L);
        entity.setName("Show");
        entity.setEventDate(LocalDate.now().plusDays(5));

        EventDto dto = new EventDto(
                101L,
                "Show",
                entity.getEventDate(),
                null,
                null,
                venueId,
                Set.of()
        );

        given(venueService.findUpcomingEventsForVenue(venueId))
                .willReturn(List.of(entity));
        given(eventMapper.toDto(entity)).willReturn(dto);

        ResponseEntity<List<EventDto>> resp =
                venueController.getUpcomingEventsForVenue(venueId);

        assertEquals(200, resp.getStatusCodeValue());
        assertEquals(List.of(dto), resp.getBody());

        verify(venueService).findUpcomingEventsForVenue(venueId);
    }
}
