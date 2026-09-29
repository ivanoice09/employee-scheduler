package com.kovic.employee_scheduler.repository;

import com.kovic.employee_scheduler.model.Week;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface WeekRepository extends JpaRepository<Week, Long> {

    // used by getWeek() from ScheduleService
    // used by saveWeek() from ScheduleService
    Optional<Week> findByDemoSessionIdAndYearAndWeekNumber(
            UUID demoSessionId,
            int year,
            int weekNumber
    );

    void deleteAllByDemoSessionId(UUID sessionId);
}
