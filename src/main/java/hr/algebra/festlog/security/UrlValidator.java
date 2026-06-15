package hr.algebra.festlog.security;

import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.List;

@Component
public class UrlValidator {

    private static final List<String> BLOCKED_HOSTS = List.of(
            "127.0.0.1",
            "localhost",
            "169.254.169.254"
    );

    public boolean validate(String urlString) {
        URI uri = URI.create(urlString);
        if (BLOCKED_HOSTS.contains(uri.getHost())) {
            throw new SecurityException("SSRF Violation: Host is blocked");
        }
        return true;
    }
}