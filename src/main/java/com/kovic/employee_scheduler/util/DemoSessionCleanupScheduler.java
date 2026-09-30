package com.kovic.employee_scheduler.util;

import com.kovic.employee_scheduler.repository.DemoSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.OffsetDateTime;

@Component
@RequiredArgsConstructor
public class DemoSessionCleanupScheduler {

    private final DemoSessionRepository demoSessionRepository;
    private static final Duration TTL = Duration.ofMinutes(3);

//    @Scheduled(fixedRateString = "3600000")
    public void cleanupExpiredSessions() {
        OffsetDateTime cutoff = OffsetDateTime.now().minus(TTL);
        demoSessionRepository.deleteByCreatedAtBefore(cutoff);
    }

}
