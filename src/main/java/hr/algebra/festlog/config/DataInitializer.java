package hr.algebra.festlog.config;

import hr.algebra.festlog.entity.Event;
import hr.algebra.festlog.entity.User;
import hr.algebra.festlog.enums.EventStatus;
import hr.algebra.festlog.enums.EventType;
import hr.algebra.festlog.enums.Role;
import hr.algebra.festlog.repository.EventRepository;
import hr.algebra.festlog.repository.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class DataInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(
        UserRepository userRepository,
        EventRepository eventRepository,
        PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (userRepository.count() > 0) return;

        User admin = new User();
        admin.setUsername("admin");
        admin.setEmail("admin@festlog.hr");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRole(Role.ADMIN);
        admin = userRepository.save(admin);

        User user = new User();
        user.setUsername("user");
        user.setEmail("user@festlog.hr");
        user.setPassword(passwordEncoder.encode("user123"));
        user.setRole(Role.USER);
        userRepository.save(user);

        createEvent("Outlook Festival 2025", "Chase & Status",
            "Fort Punta Christo", "Pula", "Croatia", "Drum & Bass",
            EventType.FESTIVAL, EventStatus.TICKETS_SECURED,
            LocalDate.of(2025, 9, 3), new BigDecimal("89.00"), null,
            "https://www.outlookfestival.com",
            "Tickets secured for the full 5-day pass. Last year was insane.", admin);

        createEvent("Glastonbury 2024", "Coldplay",
            "Worthy Farm", "Pilton", "United Kingdom", "Mixed",
            EventType.FESTIVAL, EventStatus.ATTENDED,
            LocalDate.of(2024, 6, 26), new BigDecimal("340.00"), 10,
            "https://www.glastonburyfestivals.co.uk",
            "Absolutely legendary. Beyoncé surprise set on the Park Stage. Once in a lifetime.", admin);

        createEvent("Boiler Room Zagreb", "Tijana T",
            "Tvornica Kulture", "Zagreb", "Croatia", "Techno",
            EventType.CLUB_NIGHT, EventStatus.ATTENDED,
            LocalDate.of(2024, 11, 15), new BigDecimal("25.00"), 9,
            "https://boilerroom.tv",
            "Best Boiler Room I've been to. Crowd energy was unreal.", admin);

        createEvent("Ultra Europe 2025", "Martin Garrix",
            "Poljud Stadium", "Split", "Croatia", "EDM",
            EventType.FESTIVAL, EventStatus.WISHLIST,
            LocalDate.of(2025, 7, 11), new BigDecimal("99.00"), null,
            "https://ultraeurope.com",
            "Need to decide before early bird ends. Lineup looks strong.", admin);

        createEvent("Primavera Sound 2024", "Lana Del Rey",
            "Parc del Fòrum", "Barcelona", "Spain", "Indie / Alternative",
            EventType.FESTIVAL, EventStatus.MISSED,
            LocalDate.of(2024, 5, 29), new BigDecimal("220.00"), null,
            "https://www.primaverasound.com",
            "Couldn't get flights in time. Devastated about missing Lana.", admin);

        createEvent("Dour Festival 2025", "Skrillex",
            "La Plaine de La Machine à Feu", "Dour", "Belgium", "Electronic",
            EventType.FESTIVAL, EventStatus.WISHLIST,
            LocalDate.of(2025, 7, 17), new BigDecimal("130.00"), null,
            "https://dourfestival.eu",
            "Underrated gem. Considering going with a group.", admin);

        createEvent("Massive Attack Live", "Massive Attack",
            "Fabrique", "Milan", "Italy", "Trip Hop",
            EventType.CONCERT, EventStatus.ATTENDED,
            LocalDate.of(2023, 10, 20), new BigDecimal("65.00"), 8,
            "https://www.massiveattack.com",
            "Incredible visuals and production. Set was mostly Mezzanine era.", admin);

        createEvent("Sonus Festival 2025", "Peggy Gou",
            "Zrće Beach", "Novalja", "Croatia", "House / Techno",
            EventType.OPEN_AIR, EventStatus.TICKETS_SECURED,
            LocalDate.of(2025, 8, 30), new BigDecimal("110.00"), null,
            "https://sonusfestival.com",
            "Zrće sunrise sets are unmatched. Can't wait.", admin);
    }

    private void createEvent(
        String name, String headliner, String venue, String city, String country, String genre,
        EventType type, EventStatus status, LocalDate date, BigDecimal ticket,
        Integer rating, String lineupUrl, String notes, User addedBy
    ) {
        Event event = new Event();
        event.setName(name);
        event.setHeadliner(headliner);
        event.setVenue(venue);
        event.setCity(city);
        event.setCountry(country);
        event.setGenre(genre);
        event.setEventType(type);
        event.setStatus(status);
        event.setEventDate(date);
        event.setTicketPrice(ticket);
        event.setPersonalRating(rating);
        event.setLineupUrl(lineupUrl);
        event.setNotes(notes);
        event.setAddedBy(addedBy);
        eventRepository.save(event);
    }
}
