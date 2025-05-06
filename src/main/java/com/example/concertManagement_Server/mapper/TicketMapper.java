package com.example.concertManagement_Server.mapper;

import com.example.concertManagement_Server.dto.TicketDto;
import com.example.concertManagement_Server.model.Ticket;
import org.springframework.stereotype.Component;

/** Maps between Ticket entity and its DTO representation. */
@Component
public class TicketMapper {

    /**
     * Converts a Ticket entity to its DTO.
     */
    public TicketDto toDto(Ticket t) {
        return new TicketDto(
                t.getId(),
                t.getEvent().getName(),
                t.getEvent().getEventDate(),
                t.getEvent().getVenue().getName(),
                t.getBuyerEmail()          // or getBuyerName() if you kept the old column
        );
    }
}
