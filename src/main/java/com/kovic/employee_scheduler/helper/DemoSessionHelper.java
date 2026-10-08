package com.kovic.employee_scheduler.helper;

import com.kovic.employee_scheduler.model.DemoSession;
import com.kovic.employee_scheduler.repository.DemoSessionRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DemoSessionHelper {

    private final DemoSessionRepository demoSessionRepository;

    public static final String COOKIE_NAME = "DEMO_SESSION_ID";
    public static final int MAX_AGE_SECONDS = 1800;
    public static final Duration SESSION_TTL = Duration.ofMinutes(30);

    /**
     * Returns the current valid DEMO_SESSION_ID or the cookie for this request.
     * If missing or expired, creates a new one and sets it on the response.
     */
    public Optional<UUID> readOrCreateDemoSessionRow(HttpServletRequest request, HttpServletResponse response) {

        Optional<UUID> demoSessionId = readCookie(request);

        DemoSession demoSessionRow = demoSessionId
                .flatMap(demoSessionRepository::findById)
                .orElse(null);

        if (demoSessionRow == null || isExpired(demoSessionRow)) {
            return createDemoSessionRow(response);
        }

        sendExpirationHeader(response, demoSessionRow);

        return demoSessionId;
    }

    public boolean isExpired(DemoSession session) {
        return OffsetDateTime.now().isAfter(session.getCreatedAt().plus(SESSION_TTL));
    }

    public Optional<UUID> readCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return Optional.empty();
        return Arrays.stream(cookies)
                .filter(c -> COOKIE_NAME.equals(c.getName()))
                .map(c -> UUID.fromString(c.getValue()))
                .findFirst();
    }

    private Optional<UUID> createDemoSessionRow(HttpServletResponse response) {

        UUID newDemoSessionId = UUID.randomUUID();

        // saves a new row of "demo_sessions"
        DemoSession newDemoSessionRow = new DemoSession();
        newDemoSessionRow.setId(newDemoSessionId);
        newDemoSessionRow.setCreatedAt(OffsetDateTime.now());
        newDemoSessionRow.setLastActiveAt(OffsetDateTime.now()); // unused but needed in the future
        demoSessionRepository.save(newDemoSessionRow);

        // sets the cookie
        setCookie(response, newDemoSessionId);

        // sets the cookie's expiry on the response's headers
        sendExpirationHeader(response, newDemoSessionRow);

        return Optional.of(newDemoSessionId);
    }

    private void setCookie(HttpServletResponse response, UUID demoSessionId) {
        Cookie cookie = new Cookie(COOKIE_NAME, demoSessionId.toString());
        cookie.setHttpOnly(true); // activates cookie handling on the browser
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(MAX_AGE_SECONDS);
        response.addCookie(cookie);
    }

    private void sendExpirationHeader(HttpServletResponse response, DemoSession demoSession) {
        long expiresAtMillis = demoSession
                .getCreatedAt()
                .plusSeconds(MAX_AGE_SECONDS)
                .toInstant()
                .toEpochMilli();

        response.setHeader(
                "X-Demo-Session-Expires-At",
                String.valueOf(expiresAtMillis)
        );
    }

}
