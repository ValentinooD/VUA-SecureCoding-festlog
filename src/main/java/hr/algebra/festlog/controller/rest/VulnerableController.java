package hr.algebra.festlog.controller.rest;

import hr.algebra.festlog.dto.EventDto;
import hr.algebra.festlog.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/vuln")
@Tag(name = "Vulnerable endpoints", description = "Endpoints vulnerable to attack")
public class VulnerableController {

    private final EntityManager entityManager;

    public VulnerableController(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @GetMapping("/attack")
    @Operation(summary = "Search function which is vulnerable to SQL injections")
    public ResponseEntity<?> search(
            @RequestParam(required = false) String query
    ) {
        String sql = "SELECT u FROM User u WHERE username = :username";
        TypedQuery<User> q = entityManager.createQuery(sql, User.class);
        q.setParameter("username", query);

        return ResponseEntity.ok(q.getResultList());
    }
}
