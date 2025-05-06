package com.example.concertManagement_Server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO for ticket information in responses.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketDto {
    private Long   id;
    private String eventName;
    private LocalDate eventDate;
    private String venueName;
    private String buyerEmail;
}
