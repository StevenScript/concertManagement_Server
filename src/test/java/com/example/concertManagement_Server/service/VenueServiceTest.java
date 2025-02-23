package com.example.concertManagement_Server.service;

import com.example.concertManagement_Server.model.Venue;
import com.example.concertManagement_Server.repository.VenueRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VenueServiceTest {

    @Mock
    private VenueRepository venueRepository;

    @InjectMocks
    private VenueService venueService;

    @Test
    void testGetVenueById_Found() {
        Venue mockVenue = new Venue();
        mockVenue.setId(10L);
        mockVenue.setName("Mock Venue");

        when(venueRepository.findById(10L)).thenReturn(Optional.of(mockVenue));

        Venue result = venueService.getVenueById(10L);
        Assertions.assertNotNull(result);
        Assertions.assertEquals("Mock Venue", result.getName());
        verify(venueRepository).findById(10L);
    }

    @Test
    void testGetVenueById_NotFound() {
        when(venueRepository.findById(99L)).thenReturn(Optional.empty());

        Venue result = venueService.getVenueById(99L);
        Assertions.assertNull(result, "Null if venue not found");
        verify(venueRepository).findById(99L);
    }

    @Test
    void testCreateVenue() {
        Venue newVenue = new Venue();
        newVenue.setName("New Venue");

        Venue savedVenue = new Venue();
        savedVenue.setId(123L);
        savedVenue.setName("New Venue");

        when(venueRepository.save(newVenue)).thenReturn(savedVenue);

        Venue result = venueService.createVenue(newVenue);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(123L, result.getId());
        Assertions.assertEquals("New Venue", result.getName());

        verify(venueRepository).save(newVenue);
    }

    @Test
    void testUpdateVenue_Found() {
        Venue existingVenue = new Venue();
        existingVenue.setId(5L);
        existingVenue.setName("Old Name");
        existingVenue.setLocation("City A");

        when(venueRepository.findById(5L)).thenReturn(Optional.of(existingVenue));
        when(venueRepository.save(any(Venue.class))).thenAnswer(inv -> inv.getArgument(0));

        Venue updatedData = new Venue();
        updatedData.setName("Updated Name");
        updatedData.setLocation("City B");

        Venue result = venueService.updateVenue(5L, updatedData);
        Assertions.assertNotNull(result);
        Assertions.assertEquals("Updated Name", result.getName());
        Assertions.assertEquals("City B", result.getLocation());
        verify(venueRepository).findById(5L);
        verify(venueRepository).save(any(Venue.class));
    }

    @Test
    void testUpdateVenue_NotFound() {
        when(venueRepository.findById(999L)).thenReturn(Optional.empty());

        Venue updatedData = new Venue();
        updatedData.setName("Does not matter");

        Venue result = venueService.updateVenue(999L, updatedData);
        Assertions.assertNull(result);
        verify(venueRepository).findById(999L);
        verify(venueRepository, never()).save(any(Venue.class));
    }
}
