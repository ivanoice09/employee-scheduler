package com.kovic.employee_scheduler.service;

import com.kovic.employee_scheduler.repository.DemoSessionRepository;
import com.kovic.employee_scheduler.repository.ShiftRepository;
import com.kovic.employee_scheduler.repository.WeekRepository;
import com.kovic.employee_scheduler.model.DemoSession;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class DemoCleanupService {

    private final DemoSessionRepository sessionRepository;
    private final ShiftRepository shiftRepository;
    private final WeekRepository weekRepository;

    public DemoCleanupService(
            DemoSessionRepository sessionRepository,
            ShiftRepository shiftRepository,
            WeekRepository weekRepository
    ) {
        this.sessionRepository = sessionRepository;
        this.shiftRepository = shiftRepository;
        this.weekRepository = weekRepository;
    }

    @Transactional
    public void deleteSession(UUID sessionId) {
        // Order matters: shifts reference weeks
        shiftRepository.deleteAllByDemoSessionId(sessionId);
        weekRepository.deleteAllByDemoSessionId(sessionId);
        sessionRepository.deleteById(sessionId);
    }

    @Scheduled(fixedRate = 60_000) // every hour
    @Transactional
    public void cleanupExpiredSessions() {
        OffsetDateTime cutoff = OffsetDateTime.now().minusMinutes(3);

        // First, collect IDs to delete
        List<UUID> expiredIds = sessionRepository.findAll()
                .stream()
                .filter(s -> s.getLastActiveAt().isBefore(cutoff))
                .map(DemoSession::getSessionId)
                .toList();

        for (UUID id : expiredIds) {
            deleteSession(id);
        }
    }
}
