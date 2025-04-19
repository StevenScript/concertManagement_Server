package com.example.concertManagement_Server.controller;

import com.example.concertManagement_Server.dto.ArtistDto;
import com.example.concertManagement_Server.dto.ArtistRequest;
import com.example.concertManagement_Server.exception.ResourceNotFoundException;
import com.example.concertManagement_Server.mapper.ArtistMapper;
import com.example.concertManagement_Server.model.Artist;
import com.example.concertManagement_Server.model.Event;
import com.example.concertManagement_Server.model.Venue;
import com.example.concertManagement_Server.service.ArtistService;
import com.example.concertManagement_Server.service.EventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link ArtistController}, verifying request→entity→DTO mapping
 * and correct HTTP responses.
 */
public class ArtistControllerTest {

    @Mock
    private ArtistService artistService;

    @Mock
    private EventService eventService;

    @Mock
    private ArtistMapper artistMapper;

    @InjectMocks
    private ArtistController artistController;

    /**
     * Ensures that creating an artist:
     * 1) maps the request to an entity
     * 2) calls the service to save
     * 3) maps the saved entity back to a DTO
     * and returns HTTP 201 with the DTO.
     */
    @Test
    void testCreateArtistDtoPipeline() {
        ArtistRequest req = new ArtistRequest("StageX", "Jazz", 4, "New Orleans");
        Artist entity = new Artist();
        given(artistMapper.toEntity(req)).willReturn(entity);

        Artist saved = new Artist();
        saved.setId(5L);
        saved.setStageName("StageX");
        saved.setGenre("Jazz");
        saved.setMembersCount(4);
        saved.setHomeCity("New Orleans");
        given(artistService.createArtist(entity)).willReturn(saved);

        ArtistDto dto = new ArtistDto(5L, "StageX", "Jazz", 4, "New Orleans");
        given(artistMapper.toDto(saved)).willReturn(dto);

        ResponseEntity<ArtistDto> response = artistController.createArtist(req);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(dto, response.getBody());
    }

    /** Verifies getAllArtists() returns a list of mapped DTOs. */
    @Test
    void testGetAllArtistsDtoPipeline() {
        Artist e = new Artist();
        e.setId(2L);
        e.setStageName("Alpha");
        given(artistService.getAllArtists()).willReturn(List.of(e));

        ArtistDto d = new ArtistDto(2L, "Alpha", null, null, null);
        given(artistMapper.toDto(e)).willReturn(d);

        List<ArtistDto> list = artistController.getAllArtists();

        assertEquals(1, list.size());
        assertEquals(d, list.get(0));
    }

    /** Verifies getArtist(id) returns HTTP 200 and the correct DTO. */
    @Test
    void testGetArtistByIdDto() {
        Artist entity = new Artist();
        entity.setId(3L);
        entity.setStageName("Beta");
        ArtistDto dto = new ArtistDto(3L, "Beta", null, null, null);
        given(artistService.getArtistById(3L)).willReturn(entity);
        given(artistMapper.toDto(entity)).willReturn(dto);

        ResponseEntity<ArtistDto> response = artistController.getArtist(3L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(dto, response.getBody());
    }

    /** Confirms requesting a missing artist throws ResourceNotFoundException. */
    @Test
    void testGetArtistNotFoundDto() {
        given(artistService.getArtistById(99L))
                .willThrow(new ResourceNotFoundException("Artist with id 99 not found"));

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> artistController.getArtist(99L)
        );
        assertEquals("Artist with id 99 not found", ex.getMessage());
    }

    /** Checks getEventsForArtist returns HTTP 200 with the event list. */
    @Test
    void testGetEventsForArtist() {
        Event ev = new Event();
        ev.setId(100L);
        given(eventService.listAllEventsForArtist(5L)).willReturn(List.of(ev));

        ResponseEntity<List<Event>> resp = artistController.getEventsForArtist(5L);

        assertEquals(200, resp.getStatusCodeValue());
        assertEquals(1, resp.getBody().size());
        assertEquals(100L, resp.getBody().get(0).getId());
    }

    /** Verifies getTicketCountForArtist returns HTTP 200 and the correct sum. */
    @Test
    void testGetTicketCountForArtist() {
        Long artistId = 4L;
        given(artistService.getTicketCountForArtist(artistId)).willReturn(123L);

        ResponseEntity<Long> resp = artistController.getTicketCountForArtist(artistId);
        assertEquals(200, resp.getStatusCodeValue());
        assertEquals(123L, resp.getBody());
    }

    /** Ensures getVenuesForArtist returns HTTP 200 with the venue list. */
    @Test
    void testGetVenuesForArtist() {
        Long artistId = 7L;
        Venue v = new Venue();
        v.setId(200L);
        given(artistService.getVenuesForArtist(artistId)).willReturn(List.of(v));

        ResponseEntity<List<Venue>> resp = artistController.getVenuesForArtist(artistId);
        assertEquals(200, resp.getStatusCodeValue());
        assertEquals(200L, resp.getBody().get(0).getId());
    }
}

