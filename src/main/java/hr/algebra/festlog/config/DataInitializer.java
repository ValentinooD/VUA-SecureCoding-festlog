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
import java.time.Month;

@Component
public class DataInitializer implements ApplicationRunner {

    private static final String STR_CROATIA = "Croatia";

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
        admin.setPassword(passwordEncoder.encode(System.getenv("DEFAULT_ADMIN_PASSWD")));
        admin.setRole(Role.ADMIN);
        admin = userRepository.save(admin);

        User user = new User();
        user.setUsername("user");
        user.setEmail("user@festlog.hr");
        user.setPassword(passwordEncoder.encode(System.getenv("DEFAULT_USER_PASSWD")));
        user.setRole(Role.USER);
        userRepository.save(user);

        createEvent(new Event("Outlook Festival 2025", "Chase & Status",
            "Fort Punta Christo", "Pula", STR_CROATIA, "Drum & Bass",
            EventType.FESTIVAL, EventStatus.TICKETS_SECURED,
            LocalDate.of(2025, Month.SEPTEMBER, 3), new BigDecimal("89.00"), null,
            "https://www.outlookfestival.com",
            "Tickets secured for the full 5-day pass. Last year was insane.", admin));

        createEvent(new Event("Glastonbury 2024", "Coldplay",
            "Worthy Farm", "Pilton", "United Kingdom", "Mixed",
            EventType.FESTIVAL, EventStatus.ATTENDED,
            LocalDate.of(2024, Month.JUNE, 26), new BigDecimal("340.00"), 10,
            "https://www.glastonburyfestivals.co.uk",
            "Absolutely legendary. Beyoncé surprise set on the Park Stage. Once in a lifetime.", admin));

        createEvent(new Event("Boiler Room Zagreb", "Tijana T",
            "Tvornica Kulture", "Zagreb", STR_CROATIA, "Techno",
            EventType.CLUB_NIGHT, EventStatus.ATTENDED,
            LocalDate.of(2024, Month.NOVEMBER, 15), new BigDecimal("25.00"), 9,
            "https://boilerroom.tv",
            "Best Boiler Room I've been to. Crowd energy was unreal.", admin));

        createEvent(new Event("Ultra Europe 2025", "Martin Garrix",
            "Poljud Stadium", "Split", STR_CROATIA, "EDM",
            EventType.FESTIVAL, EventStatus.WISHLIST,
            LocalDate.of(2025, Month.JULY, 11), new BigDecimal("99.00"), null,
            "https://ultraeurope.com",
            "Need to decide before early bird ends. Lineup looks strong.", admin));

        createEvent(new Event("Primavera Sound 2024", "Lana Del Rey",
            "Parc del Fòrum", "Barcelona", "Spain", "Indie / Alternative",
            EventType.FESTIVAL, EventStatus.MISSED,
            LocalDate.of(2024, Month.MAY, 29), new BigDecimal("220.00"), null,
            "https://www.primaverasound.com",
            "Couldn't get flights in time. Devastated about missing Lana.", admin));

        createEvent(new Event("Dour Festival 2025", "Skrillex",
            "La Plaine de La Machine à Feu", "Dour", "Belgium", "Electronic",
            EventType.FESTIVAL, EventStatus.WISHLIST,
            LocalDate.of(2025, Month.JULY, 17), new BigDecimal("130.00"), null,
            "https://dourfestival.eu",
            "Underrated gem. Considering going with a group.", admin));

        createEvent(new Event("Massive Attack Live", "Massive Attack",
            "Fabrique", "Milan", "Italy", "Trip Hop",
            EventType.CONCERT, EventStatus.ATTENDED,
            LocalDate.of(2023, Month.OCTOBER, 20), new BigDecimal("65.00"), 8,
            "https://www.massiveattack.com",
            "Incredible visuals and production. Set was mostly Mezzanine era.", admin));

        createEvent(new Event("Sonus Festival 2025", "Peggy Gou",
            "Zrće Beach", "Novalja", STR_CROATIA, "House / Techno",
            EventType.OPEN_AIR, EventStatus.TICKETS_SECURED,
            LocalDate.of(2025, Month.AUGUST, 30), new BigDecimal("110.00"), null,
            "https://sonusfestival.com",
            "Zrće sunrise sets are unmatched. Can't wait.", admin));
    }

    private void createEvent(Event event) {
        eventRepository.save(event);
    }
}
