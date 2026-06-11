package hr.algebra.festlog.controller.rest;

import hr.algebra.festlog.dto.EventDto;
import hr.algebra.festlog.entity.User;
import hr.algebra.festlog.enums.EventStatus;
import hr.algebra.festlog.enums.EventType;
import hr.algebra.festlog.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/events")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Events", description = "Festival and concert CRUD and search")
public class EventRestController {

    private final EventService eventService;

    public EventRestController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    @Operation(summary = "Get all events")
    public ResponseEntity<List<EventDto>> getAll() {
        return ResponseEntity.ok(eventService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a single event by ID")
    public ResponseEntity<EventDto> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(eventService.findById(id));
        } catch (NoSuchElementException _) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    @Operation(summary = "Search events by name, headliner, type, status or city")
    public ResponseEntity<List<EventDto>> search(
        @RequestParam(required = false) String query,
        @RequestParam(required = false) EventType type,
        @RequestParam(required = false) EventStatus status,
        @RequestParam(required = false) String city
    ) {
        return ResponseEntity.ok(eventService.search(query, type, status, city));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new event entry (admin only)")
    public ResponseEntity<EventDto> create(
        @Valid @RequestBody EventDto dto,
        @AuthenticationPrincipal User currentUser
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.create(dto, currentUser));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update an event entry (admin only)")
    public ResponseEntity<EventDto> update(
        @PathVariable Long id,
        @Valid @RequestBody EventDto dto
    ) {
        try {
            return ResponseEntity.ok(eventService.update(id, dto));
        } catch (NoSuchElementException _) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete an event entry (admin only)")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            eventService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException _) {
            return ResponseEntity.notFound().build();
        }
    }
}
