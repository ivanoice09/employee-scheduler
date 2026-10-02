package com.kovic.employee_scheduler.util;

import com.kovic.employee_scheduler.model.DemoSession;
import com.kovic.employee_scheduler.service.DemoSessionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;
import java.util.UUID;

/**
 *  a convenience wrapper so services can get the current session ID without
 *  needing HttpServletRequest/HttpServletResponse in their method signatures.
 */
@Component
@RequiredArgsConstructor
public class DemoSessionContext {

    private final DemoSessionService demoSessionService;

    /**
     * Returns the current valid session ID for this HTTP request.
     * Creates a new session (and cookie) if missing or expired.
     */
    public UUID getRequired() {

        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(
                RequestContextHolder.getRequestAttributes()
        )).getRequest();

        HttpServletResponse response = ((ServletRequestAttributes) Objects.requireNonNull(
                RequestContextHolder.getRequestAttributes()
        )).getResponse();

        DemoSession demoSession = demoSessionService.getOrCreateSession(request, response);

        return demoSession.getDemoSessionId();
    }

    public long getExpiryEpochMs() {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(
                RequestContextHolder.getRequestAttributes()
        )).getRequest();

        HttpServletResponse response = ((ServletRequestAttributes) Objects.requireNonNull(
                RequestContextHolder.getRequestAttributes()
        )).getResponse();

        DemoSession session = demoSessionService.getOrCreateSession(request, response);
        return session.getCreatedAt()
                .plus(DemoSessionService.SESSION_TTL)
                .toInstant()
                .toEpochMilli();
    }

}
