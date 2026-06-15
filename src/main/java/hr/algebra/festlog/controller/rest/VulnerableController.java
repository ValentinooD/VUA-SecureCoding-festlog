package hr.algebra.festlog.controller.rest;

import hr.algebra.festlog.entity.User;
import hr.algebra.festlog.security.UrlValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/vuln")
@Tag(name = "Vulnerable endpoints", description = "Endpoints vulnerable to attack")
public class VulnerableController {

    private final EntityManager entityManager;
    private final UrlValidator urlValidator;

    public VulnerableController(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.urlValidator = new UrlValidator();
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

    @PostMapping("/ssrf")
    public ResponseEntity<String> testSsrf(@RequestParam String url) {
        try {
            if (urlValidator.validate(url)) {
                return ResponseEntity.ok("Request allowed to: " + url);
            }
            return ResponseEntity.badRequest().build();
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }
}
