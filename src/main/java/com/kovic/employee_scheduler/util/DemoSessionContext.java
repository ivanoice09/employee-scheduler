package com.kovic.employee_scheduler.util;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DemoSessionContext {

    private static final ThreadLocal<UUID> CURRENT = new ThreadLocal<>();

    public void set(UUID sessionId) {
        CURRENT.set(sessionId);
    }

    public UUID getRequired() {
        UUID sessionId = CURRENT.get();
        if (sessionId == null) {
            throw new IllegalStateException("Demo session is not available");
        }
        return sessionId;
    }

    public void clear() {
        CURRENT.remove();
    }
}
