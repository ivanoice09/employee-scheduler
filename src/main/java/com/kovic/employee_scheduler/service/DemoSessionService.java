package com.kovic.employee_scheduler.service;

import com.kovic.employee_scheduler.model.DemoSession;
import com.kovic.employee_scheduler.repository.DemoSessionRepository;
import com.kovic.employee_scheduler.repository.ShiftRepository;
import com.kovic.employee_scheduler.repository.WeekRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DemoSessionService {

    private final DemoSessionRepository demoSessionRepository;
    private final ShiftRepository shiftRepository;
    private final WeekRepository weekRepository;

    public static final String COOKIE_NAME = "DEMO_SESSION";
    public static final int MAX_AGE_SECONDS = 180;
    public static final Duration SESSION_TTL = Duration.ofMinutes(3);

    /**
     * Returns the current valid session for this request.
     * If missing or expired, creates a new one and sets the cookie on the response.
     */
    public DemoSession getOrCreateSession(HttpServletRequest request, HttpServletResponse response) {
        Optional<UUID> sessionIdOpt = readCookie(request, COOKIE_NAME);
        DemoSession session = sessionIdOpt
                .flatMap(demoSessionRepository::findById)
                .orElse(null);

        if (session == null || isExpired(session)) {
            if (session != null) {
                cleanupDemoData(session.getDemoSessionId());
                demoSessionRepository.delete(session);
            }
            return createSession(response);
        }

        return session;
    }

    public boolean isExpired(DemoSession session) {
        return OffsetDateTime.now().isAfter(session.getCreatedAt().plus(SESSION_TTL));
    }

    public void cleanupDemoData(UUID demoSessionId) {
        DemoSession demoSessionRef = demoSessionRepository.findById(demoSessionId)
                .orElseThrow(() -> new IllegalStateException("Demo session not found"));
        shiftRepository.deleteByDemoSession(demoSessionRef);
        weekRepository.deleteByDemoSession(demoSessionRef);
    }

    public Optional<UUID> readCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return Optional.empty();
        return Arrays.stream(cookies)
                .filter(c -> name.equals(c.getName()))
                .map(c -> UUID.fromString(c.getValue()))
                .findFirst();
    }

    private DemoSession createSession(HttpServletResponse response) {
        UUID demoSessionId = UUID.randomUUID();

        DemoSession session = new DemoSession();
        session.setDemoSessionId(demoSessionId);
        session.setCreatedAt(OffsetDateTime.now());
        session.setLastActiveAt(OffsetDateTime.now());
        demoSessionRepository.save(session);

        Cookie cookie = new Cookie(COOKIE_NAME, demoSessionId.toString());
        cookie.setHttpOnly(false);
        cookie.setPath("/");
        cookie.setMaxAge(MAX_AGE_SECONDS);
        response.addCookie(cookie);

        return session;
    }
}
