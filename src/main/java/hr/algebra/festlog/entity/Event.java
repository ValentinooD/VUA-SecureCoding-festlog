package hr.algebra.festlog.entity;

import hr.algebra.festlog.enums.EventStatus;
import hr.algebra.festlog.enums.EventType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 200)
    @Column(nullable = false, length = 200)
    private String name;

    @NotBlank
    @Size(max = 200)
    @Column(nullable = false, length = 200)
    private String headliner;

    @Size(max = 150)
    private String venue;

    @Size(max = 100)
    private String city;

    @Size(max = 100)
    private String country;

    @Size(max = 100)
    private String genre;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventType eventType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventStatus status;

    private LocalDate eventDate;

    @DecimalMin("0.00")
    @Column(precision = 10, scale = 2)
    private BigDecimal ticketPrice;

    @Min(1) @Max(10)
    private Integer personalRating;

    @Size(max = 500)
    @Column(length = 500)
    private String lineupUrl;

    @Size(max = 2000)
    @Column(length = 2000)
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "added_by_id")
    private User addedBy;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId()                           { return id; }
    public void setId(Long id)                    { this.id = id; }
    public String getName()                       { return name; }
    public void setName(String name)              { this.name = name; }
    public String getHeadliner()                  { return headliner; }
    public void setHeadliner(String headliner)    { this.headliner = headliner; }
    public String getVenue()                      { return venue; }
    public void setVenue(String venue)            { this.venue = venue; }
    public String getCity()                       { return city; }
    public void setCity(String city)              { this.city = city; }
    public String getCountry()                    { return country; }
    public void setCountry(String country)        { this.country = country; }
    public String getGenre()                      { return genre; }
    public void setGenre(String genre)            { this.genre = genre; }
    public EventType getEventType()               { return eventType; }
    public void setEventType(EventType t)         { this.eventType = t; }
    public EventStatus getStatus()                { return status; }
    public void setStatus(EventStatus s)          { this.status = s; }
    public LocalDate getEventDate()               { return eventDate; }
    public void setEventDate(LocalDate eventDate) { this.eventDate = eventDate; }
    public BigDecimal getTicketPrice()            { return ticketPrice; }
    public void setTicketPrice(BigDecimal p)      { this.ticketPrice = p; }
    public Integer getPersonalRating()            { return personalRating; }
    public void setPersonalRating(Integer r)      { this.personalRating = r; }
    public String getLineupUrl()                  { return lineupUrl; }
    public void setLineupUrl(String lineupUrl)    { this.lineupUrl = lineupUrl; }
    public String getNotes()                      { return notes; }
    public void setNotes(String notes)            { this.notes = notes; }
    public User getAddedBy()                      { return addedBy; }
    public void setAddedBy(User addedBy)          { this.addedBy = addedBy; }
    public LocalDateTime getCreatedAt()           { return createdAt; }
    public void setCreatedAt(LocalDateTime t)     { this.createdAt = t; }
    public LocalDateTime getUpdatedAt()           { return updatedAt; }
    public void setUpdatedAt(LocalDateTime t)     { this.updatedAt = t; }
}
