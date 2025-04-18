package com.example.concertManagement_Server.mapper;

import com.example.concertManagement_Server.dto.VenueDto;
import com.example.concertManagement_Server.dto.VenueRequest;
import com.example.concertManagement_Server.model.Venue;
import org.springframework.stereotype.Component;

@Component
public class VenueMapper {
    public VenueDto toDto(Venue v) {
        return new VenueDto(v.getId(), v.getName(), v.getLocation(), v.getCapacity());
    }

    public Venue toEntity(VenueRequest req) {
        Venue v = new Venue();
        v.setName(req.getName());
        v.setLocation(req.getLocation());
        v.setCapacity(req.getCapacity());
        return v;
    }

    public void updateEntity(VenueRequest req, Venue v) {
        v.setName(req.getName());
        v.setLocation(req.getLocation());
        v.setCapacity(req.getCapacity());
    }
}
