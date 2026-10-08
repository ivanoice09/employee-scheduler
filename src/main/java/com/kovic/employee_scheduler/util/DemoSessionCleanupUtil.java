package com.kovic.employee_scheduler.util;

import com.kovic.employee_scheduler.repository.DemoSessionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Component
@RequiredArgsConstructor
public class DemoSessionCleanupUtil {

    private final DemoSessionRepository demoSessionRepository;

    /**
     * Periodically removes expired demo sessions.
     * "fixedDelay" means the next execution starts [fixedDelay] after the previous execution finishes:
     * 1 hour = 3_600_000 (actual)
     * 1 minute = 60_000 (for testing)
     */
    @Scheduled(fixedDelay = 3_600_000)
    @Transactional
    public void removeExpiredSession() {
        OffsetDateTime expirationTime = OffsetDateTime.now().minusMinutes(30);
        demoSessionRepository.deleteByCreatedAtBefore(expirationTime);
    }
}
