package com.kovic.employee_scheduler.repository;

import com.kovic.employee_scheduler.model.DemoSession;
import com.kovic.employee_scheduler.model.Week;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface WeekRepository extends JpaRepository<Week, Long> {

    Week findByYearAndWeekNumber(int year, int weekNumber);

    Optional<Week> findByDemoSessionAndYearAndWeekNumber(
            DemoSession demoSession,
            int year,
            int weekNumber
    );

    void deleteByDemoSession(DemoSession demoSession);
}
