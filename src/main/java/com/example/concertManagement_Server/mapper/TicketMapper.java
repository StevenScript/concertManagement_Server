package com.example.concertManagement_Server.mapper;

import com.example.concertManagement_Server.dto.TicketDto;
import com.example.concertManagement_Server.model.Event;
import com.example.concertManagement_Server.model.Ticket;
import org.springframework.stereotype.Component;

/** Maps between Ticket entity and its DTO representation. */
@Component
public class TicketMapper {

    /**
     * Converts a Ticket entity to its DTO.
     */
    public TicketDto toDto(Ticket t) {
        Event e = t.getEvent();
        return new TicketDto(
                t.getId(),
                e.getName(),
                e.getEventDate(),
                e.getVenue() != null ? e.getVenue().getName() : "—",
                t.getBuyerEmail()
        );
    }
}
