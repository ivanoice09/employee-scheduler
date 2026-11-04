package com.kovic.employee_scheduler.repository;

import com.kovic.employee_scheduler.model.Week;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface WeekRepository extends JpaRepository<Week, Long> {

    Optional<Week> findByYearAndWeekNumberAndDemoSessionId(
            int year,
            int weekNumber,
            UUID demoSessionId
    );

}
