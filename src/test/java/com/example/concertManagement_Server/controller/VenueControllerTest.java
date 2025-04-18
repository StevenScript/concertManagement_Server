package com.example.concertManagement_Server.controller;

import com.example.concertManagement_Server.dto.VenueDto;
import com.example.concertManagement_Server.dto.VenueRequest;
import com.example.concertManagement_Server.mapper.VenueMapper;
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

import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VenueControllerTest {

    @Mock
    private VenueService venueService;

    @Mock
    private ArtistService artistService;

    @Mock
    private VenueMapper venueMapper;

    @InjectMocks
    private VenueController venueController;

    @Test
    void testGetVenue_Found() {
        Venue entity = new Venue(); entity.setId(100L); entity.setName("Test");
        VenueDto dto = new VenueDto(100L, "Test", null, null);
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
        Venue entity = new Venue(); entity.setId(5L); entity.setName("Name");
        VenueDto dto = new VenueDto(5L, "Name", "Loc", 123);
        given(venueService.createVenue(req)).willReturn(entity);
        given(venueMapper.toDto(entity)).willReturn(dto);

        ResponseEntity<VenueDto> resp = venueController.createVenue(req);
        assertEquals(201, resp.getStatusCodeValue());
        assertEquals(dto, resp.getBody());
    }

    @Test
    void testUpdateVenue() {
        VenueRequest req = new VenueRequest("Up","Loc2",200);
        Venue entity = new Venue(); entity.setId(7L); entity.setName("Up");
        VenueDto dto = new VenueDto(7L, "Up", "Loc2", 200);
        given(venueService.updateVenue(7L, req)).willReturn(entity);
        given(venueMapper.toDto(entity)).willReturn(dto);

        ResponseEntity<VenueDto> resp = venueController.updateVenue(7L, req);
        assertEquals(200, resp.getStatusCodeValue());
        assertEquals(dto, resp.getBody());
    }

    @Test
    void testGetArtistsForVenue() {
        Artist mockArtist = new Artist();
        mockArtist.setId(100L);
        mockArtist.setStageName("The Testers");

        when(artistService.listAllArtistsForVenue(5L))
                .thenReturn(Collections.singletonList(mockArtist));

        ResponseEntity<List<Artist>> response = venueController.getArtistsForVenue(5L);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(100L, response.getBody().get(0).getId());

        verify(artistService).listAllArtistsForVenue(5L);
    }

    @Test
    void testGetUpcomingEventsForVenue() {
        // Prepare a venue ID and a list of upcoming events
        Long venueId = 1L;
        Event upcomingEvent = new Event();
        upcomingEvent.setId(101L);
        upcomingEvent.setEventDate(LocalDate.now().plusDays(5));
        List<Event> upcomingEvents = Collections.singletonList(upcomingEvent);

        // Mock the service call
        when(venueService.findUpcomingEventsForVenue(venueId)).thenReturn(upcomingEvents);

        // Call your new endpoint
        ResponseEntity<List<Event>> response = venueController.getUpcomingEventsForVenue(venueId);

        // Assertions
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(101L, response.getBody().get(0).getId());

        verify(venueService).findUpcomingEventsForVenue(venueId);
    }
}
