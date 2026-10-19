package com.kovic.employee_scheduler.util;

import com.kovic.employee_scheduler.config.DemoSessionProperties;
import com.kovic.employee_scheduler.repository.DemoSessionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Component
@RequiredArgsConstructor
public class DemoSessionCleanupUtil {

    private static final Logger log = LoggerFactory.getLogger(DemoSessionCleanupUtil.class);
    private final DemoSessionRepository demoSessionRepository;
    private final DemoSessionProperties demoSessionProperties;

    @Scheduled(
            cron = "${app.demo.cleanup-cron}",
            zone = "Europe/Rome"
    )
    @Transactional
    public void removeExpiredSession() {
        deleteExpiredSessions();
    }

    @Transactional
    public void deleteExpiredSessions() {
        OffsetDateTime expirationTime = OffsetDateTime.now()
                .minus(demoSessionProperties.sessionTtl());

        int deleted = demoSessionRepository
                .deleteByCreatedAtBefore(expirationTime);

        log.info("Deleted {} expired demo sessions older than {}", deleted, expirationTime);
    }
}
