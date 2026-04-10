package hr.algebra.festlog.repository;

import hr.algebra.festlog.entity.Event;
import hr.algebra.festlog.enums.EventStatus;
import hr.algebra.festlog.enums.EventType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("""
        SELECT e FROM Event e
        WHERE (:query IS NULL OR LOWER(e.name) LIKE LOWER(CONCAT('%', :query, '%'))
                              OR LOWER(e.headliner) LIKE LOWER(CONCAT('%', :query, '%')))
          AND (:type IS NULL OR e.eventType = :type)
          AND (:status IS NULL OR e.status = :status)
          AND (:city IS NULL OR LOWER(e.city) LIKE LOWER(CONCAT('%', :city, '%')))
        ORDER BY e.eventDate DESC, e.createdAt DESC
        """)
    List<Event> search(
        @Param("query") String query,
        @Param("type") EventType type,
        @Param("status") EventStatus status,
        @Param("city") String city
    );

    List<Event> findAllByOrderByEventDateDesc();
}
