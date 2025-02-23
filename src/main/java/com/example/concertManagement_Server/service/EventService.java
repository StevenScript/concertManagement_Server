package com.example.concertManagement_Server.service;

import com.example.concertManagement_Server.model.Event;
import com.example.concertManagement_Server.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event getEventById(Long id) {
        Optional<Event> optional = eventRepository.findById(id);
        return optional.orElse(null);
    }
}
