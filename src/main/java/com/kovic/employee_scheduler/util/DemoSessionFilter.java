package com.kovic.employee_scheduler.util;

import com.kovic.employee_scheduler.model.DemoSession;
import com.kovic.employee_scheduler.repository.DemoSessionRepository;
import com.kovic.employee_scheduler.service.DemoCleanupService;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DemoSessionFilter implements Filter {

    private static final String COOKIE_NAME = "DEMO_SESSION";
    private static final int MAX_AGE_SECONDS = 30 * 60;

    private final DemoSessionContext context;
    private final DemoSessionRepository sessionRepository;

    public DemoSessionFilter(DemoSessionContext context,
                             DemoSessionRepository sessionRepository) {
        this.context = context;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if (!httpRequest.getRequestURI().startsWith("/api/")) {
            chain.doFilter(request, response);
            return;
        }

        UUID sessionId = readCookie(httpRequest)
                .orElseGet(() -> createCookie(httpResponse));

        context.set(sessionId);

        try {
            chain.doFilter(request, response);
        } finally {
            context.clear();
        }
    }

    private Optional<UUID> readCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return Optional.empty();
        }

        for (Cookie cookie : cookies) {
            if (COOKIE_NAME.equals(cookie.getName())) {
                try {
                    UUID sessionId = UUID.fromString(cookie.getValue());
                    sessionRepository.findBySessionId(sessionId).ifPresent(session -> {
                        session.setLastActiveAt(OffsetDateTime.now());
                        sessionRepository.save(session);
                    });
                    return Optional.of(sessionId);
                } catch (IllegalArgumentException ignored) {
                    return Optional.empty();
                }
            }
        }

        return Optional.empty();
    }

    private UUID createCookie(HttpServletResponse response) {
        UUID sessionId = UUID.randomUUID();

        DemoSession session = new DemoSession();
        session.setSessionId(sessionId);
        session.setCreatedAt(OffsetDateTime.now());
        session.setLastActiveAt(OffsetDateTime.now());

        sessionRepository.save(session);

        Cookie cookie = new Cookie(COOKIE_NAME, sessionId.toString());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(MAX_AGE_SECONDS);
        // cookie.setSecure(true);

        response.addCookie(cookie);
        return sessionId;
    }
}
