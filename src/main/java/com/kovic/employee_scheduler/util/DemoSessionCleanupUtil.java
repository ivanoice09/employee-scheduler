package com.kovic.employee_scheduler.util;

import com.kovic.employee_scheduler.repository.DemoSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Component
@RequiredArgsConstructor
public class DemoSessionCleanupUtil {

    private final DemoSessionRepository demoSessionRepository;
    private final DemoSessionUtil demoSessionUtil;

    /**
     * Periodically removes expired demo sessions.
     *
     * Request-based cleanup only runs when a user makes a request. If a user
     * stops interacting with the application, expired sessions could remain
     * in the database. This scheduled task periodically searches for sessions
     * inactive for at least three minutes and deletes them.
     */
    @Scheduled(fixedDelay // means the next execution starts one minute after the previous execution finishes
            = 60_000
    ) // 1 minute
    public void removeExpiredSession() {
        OffsetDateTime expirationTime = OffsetDateTime.now().minusMinutes(3);

        demoSessionRepository.findByLastActiveAtBefore(expirationTime)
                .forEach(demoSession ->
                        demoSessionUtil.deleteExpiredSession(demoSession.getId()));
    }
}
