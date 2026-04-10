package hr.algebra.festlog.dto;

import hr.algebra.festlog.entity.Event;
import hr.algebra.festlog.enums.EventStatus;
import hr.algebra.festlog.enums.EventType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "Music event / festival data transfer object")
public record EventDto(

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    Long id,

    @NotBlank @Size(max = 200)
    @Schema(description = "Event name", example = "Outlook Festival 2025")
    String name,

    @NotBlank @Size(max = 200)
    @Schema(description = "Headliner / main act", example = "Chase & Status")
    String headliner,

    @Size(max = 150)
    @Schema(description = "Venue name", example = "Fort Punta Christo")
    String venue,

    @Size(max = 100)
    @Schema(description = "City", example = "Pula")
    String city,

    @Size(max = 100)
    @Schema(description = "Country", example = "Croatia")
    String country,

    @Size(max = 100)
    @Schema(description = "Genre", example = "Drum & Bass")
    String genre,

    @NotNull
    @Schema(description = "Event type")
    EventType eventType,

    @NotNull
    @Schema(description = "Personal status")
    EventStatus status,

    @Schema(description = "Event date", example = "2025-09-04")
    LocalDate eventDate,

    @DecimalMin("0.00")
    @Schema(description = "Ticket price in EUR", example = "89.00")
    BigDecimal ticketPrice,

    @Min(1) @Max(10)
    @Schema(description = "Personal rating 1–10", example = "9")
    Integer personalRating,

    @Size(max = 500)
    @Schema(description = "Lineup / event page URL")
    String lineupUrl,

    @Size(max = 2000)
    @Schema(description = "Personal notes")
    String notes,

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    String addedBy,

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    LocalDateTime createdAt,

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    LocalDateTime updatedAt
) {
    public static EventDto from(Event event) {
        return new EventDto(
            event.getId(),
            event.getName(),
            event.getHeadliner(),
            event.getVenue(),
            event.getCity(),
            event.getCountry(),
            event.getGenre(),
            event.getEventType(),
            event.getStatus(),
            event.getEventDate(),
            event.getTicketPrice(),
            event.getPersonalRating(),
            event.getLineupUrl(),
            event.getNotes(),
            event.getAddedBy() != null ? event.getAddedBy().getUsername() : null,
            event.getCreatedAt(),
            event.getUpdatedAt()
        );
    }

    public void applyTo(Event event) {
        event.setName(name);
        event.setHeadliner(headliner);
        event.setVenue(venue);
        event.setCity(city);
        event.setCountry(country);
        event.setGenre(genre);
        event.setEventType(eventType);
        event.setStatus(status);
        event.setEventDate(eventDate);
        event.setTicketPrice(ticketPrice);
        event.setPersonalRating(personalRating);
        event.setLineupUrl(lineupUrl);
        event.setNotes(notes);
    }
}
