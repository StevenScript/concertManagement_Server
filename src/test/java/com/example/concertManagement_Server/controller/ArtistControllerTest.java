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

@ExtendWith(MockitoExtension.class)
public class ArtistControllerTest {

    @Mock
    private ArtistService artistService;

    @Mock
    private EventService eventService;

    @Mock
    private ArtistMapper artistMapper;

    @InjectMocks
    private ArtistController artistController;

    @Test
    void testCreateArtistDtoPipeline() {
        // Given: a DTO request
        ArtistRequest req = new ArtistRequest("StageX", "Jazz", 4, "New Orleans");
        // Map request to entity
        Artist entity = new Artist();
        entity.setStageName("StageX");
        given(artistMapper.toEntity(req)).willReturn(entity);

        // Service returns saved entity
        Artist saved = new Artist();
        saved.setId(5L);
        saved.setStageName("StageX");
        saved.setGenre("Jazz");
        saved.setMembersCount(4);
        saved.setHomeCity("New Orleans");
        given(artistService.createArtist(entity)).willReturn(saved);

        // Map saved entity to DTO
        ArtistDto dto = new ArtistDto(5L, "StageX", "Jazz", 4, "New Orleans");
        given(artistMapper.toDto(saved)).willReturn(dto);

        // When
        ResponseEntity<ArtistDto> response = artistController.createArtist(req);

        // Then
        assertEquals(201, response.getStatusCodeValue());
        assertEquals(dto, response.getBody());
    }

    @Test
    void testGetAllArtistsDtoPipeline() {
        // Given: a service list
        Artist e = new Artist(); e.setId(2L); e.setStageName("Alpha");
        given(artistService.getAllArtists()).willReturn(Collections.singletonList(e));

        ArtistDto d = new ArtistDto(2L, "Alpha", null, null, null);
        given(artistMapper.toDto(e)).willReturn(d);

        // When
        List<ArtistDto> list = artistController.getAllArtists();

        // Then
        assertEquals(1, list.size());
        assertEquals(d, list.get(0));
    }

    @Test
    void testGetArtistByIdDto() {
        // Given
        Artist entity = new Artist(); entity.setId(3L); entity.setStageName("Beta");
        ArtistDto dto = new ArtistDto(3L, "Beta", null, null, null);
        given(artistService.getArtistById(3L)).willReturn(entity);
        given(artistMapper.toDto(entity)).willReturn(dto);

        // When
        ResponseEntity<ArtistDto> response = artistController.getArtist(3L);

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(dto, response.getBody());
    }

    @Test
    void testGetArtistNotFoundDto() {
        // Given
        given(artistService.getArtistById(99L)).willThrow(new ResourceNotFoundException("Artist with id 99 not found"));

        // When/Then
        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> artistController.getArtist(99L)
        );
        assertEquals("Artist with id 99 not found", ex.getMessage());
    }

    @Test
    void testGetEventsForArtist() {
        // Given
        Event ev = new Event(); ev.setId(100L);
        given(eventService.listAllEventsForArtist(5L)).willReturn(Collections.singletonList(ev));

        // When
        ResponseEntity<List<Event>> resp = artistController.getEventsForArtist(5L);

        // Then
        assertEquals(200, resp.getStatusCodeValue());
        assertEquals(1, resp.getBody().size());
        assertEquals(100L, resp.getBody().get(0).getId());
    }

    @Test
    void testGetTicketCountForArtist() {
        Long artistId = 4L;
        given(artistService.getTicketCountForArtist(artistId)).willReturn(123L);

        ResponseEntity<Long> resp = artistController.getTicketCountForArtist(artistId);
        assertEquals(200, resp.getStatusCodeValue());
        assertEquals(123L, resp.getBody());
    }

    @Test
    void testGetVenuesForArtist() {
        Long artistId = 7L;
        Venue v = new Venue(); v.setId(200L);
        given(artistService.getVenuesForArtist(artistId)).willReturn(Collections.singletonList(v));

        ResponseEntity<List<Venue>> resp = artistController.getVenuesForArtist(artistId);
        assertEquals(200, resp.getStatusCodeValue());
        assertEquals(200L, resp.getBody().get(0).getId());
    }
}

