package com.kovic.employee_scheduler.util;

import com.kovic.employee_scheduler.model.DemoSession;
import com.kovic.employee_scheduler.repository.DemoSessionRepository;
import com.kovic.employee_scheduler.service.DemoSessionService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DemoSessionFilter implements Filter {

    private final DemoSessionService demoSessionService;
    private final DemoSessionRepository demoSessionRepository;

    private static final String COOKIE_NAME = DemoSessionService.COOKIE_NAME;

    @Override
    public void doFilter(
            ServletRequest req,
            ServletResponse res,
            FilterChain chain
    ) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String path = request.getRequestURI();
        if (!path.startsWith("/api/")) {
            chain.doFilter(req, res);
            return;
        }

        Optional<UUID> sessionIdOpt = demoSessionService.readCookie(request, COOKIE_NAME);

        if (sessionIdOpt.isEmpty()) {
            chain.doFilter(req, res);
            return;
        }

        UUID sessionId = sessionIdOpt.get();
        DemoSession session = demoSessionRepository.findById(sessionId).orElse(null);

        if (session == null || demoSessionService.isExpired(session)) {
            demoSessionService.cleanupDemoData(sessionId);
            if (session != null) {
                demoSessionRepository.delete(session);
            }
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Demo session expired");
            return;
        }

        chain.doFilter(req, res);
    }
}
