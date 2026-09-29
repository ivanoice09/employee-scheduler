package com.kovic.employee_scheduler.controller;

import com.kovic.employee_scheduler.dto.DemoSessionInfoDTO;
import com.kovic.employee_scheduler.dto.SaveWeekDTO;
import com.kovic.employee_scheduler.dto.WeekDTO;
import com.kovic.employee_scheduler.model.DemoSession;
import com.kovic.employee_scheduler.repository.DemoSessionRepository;
import com.kovic.employee_scheduler.service.ScheduleService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/schedule")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final DemoSessionRepository demoSessionRepository;

    public ScheduleController(ScheduleService scheduleService, DemoSessionRepository demoSessionRepository) {
        this.scheduleService = scheduleService;
        this.demoSessionRepository = demoSessionRepository;
    }

    @GetMapping("/{year}/{week}")
    public WeekDTO getWeek(@PathVariable int year, @PathVariable int week) {
        return scheduleService.getWeek(year, week);
    }

    @PostMapping("/save")
    public void saveWeek(@RequestBody SaveWeekDTO dto) {
        scheduleService.saveWeek(dto);
    }

    @GetMapping("/session-info")
    public DemoSessionInfoDTO getSessionInfo(HttpServletRequest request) {
        UUID sessionId = (UUID) request.getAttribute("DEMO_SESSION_ID");
        if (sessionId == null) {
            // No demo session (shouldn't happen if filter runs)
            return null;
        }

        DemoSession session = demoSessionRepository.findBySessionId(sessionId)
                .orElseThrow();

        int ttlSeconds = 3 * 60; // must match your cleanup threshold
        OffsetDateTime expiresAt = session.getLastActiveAt().plusSeconds(ttlSeconds);
        long remainingSeconds = java.time.Duration.between(
                OffsetDateTime.now(),
                expiresAt
        ).getSeconds();

        return new DemoSessionInfoDTO(expiresAt, remainingSeconds);
    }
}
