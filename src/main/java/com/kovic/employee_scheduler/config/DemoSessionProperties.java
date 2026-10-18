package com.kovic.employee_scheduler.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * Needed for changing expiry and demo session cleanup time in one single file
 */
@ConfigurationProperties(prefix = "app.demo")
public record DemoSessionProperties(
        long sessionTtlMinutes
) {
    public Duration sessionTtl() {
        return Duration.ofMinutes(sessionTtlMinutes);
    }

    public int maxAgeSeconds() {
        return Math.toIntExact(sessionTtl().toSeconds());
    }
}
