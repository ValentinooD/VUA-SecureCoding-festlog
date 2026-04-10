package hr.algebra.festlog.service;

import hr.algebra.festlog.dto.EventDto;
import hr.algebra.festlog.entity.Event;
import hr.algebra.festlog.entity.User;
import hr.algebra.festlog.enums.EventStatus;
import hr.algebra.festlog.enums.EventType;
import hr.algebra.festlog.repository.EventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<EventDto> findAll() {
        return eventRepository.findAllByOrderByEventDateDesc()
            .stream()
            .map(EventDto::from)
            .toList();
    }

    public EventDto findById(Long id) {
        return eventRepository.findById(id)
            .map(EventDto::from)
            .orElseThrow(() -> new NoSuchElementException("Event not found: " + id));
    }

    public List<EventDto> search(String query, EventType type, EventStatus status, String city) {
        String nq = (query != null && query.isBlank()) ? null : query;
        String nc = (city != null && city.isBlank()) ? null : city;
        return eventRepository.search(nq, type, status, nc)
            .stream()
            .map(EventDto::from)
            .toList();
    }

    @Transactional
    public EventDto create(EventDto dto, User creator) {
        Event event = new Event();
        dto.applyTo(event);
        event.setAddedBy(creator);
        return EventDto.from(eventRepository.save(event));
    }

    @Transactional
    public EventDto update(Long id, EventDto dto) {
        Event event = eventRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Event not found: " + id));
        dto.applyTo(event);
        return EventDto.from(eventRepository.save(event));
    }

    @Transactional
    public void delete(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new NoSuchElementException("Event not found: " + id);
        }
        eventRepository.deleteById(id);
    }
}
