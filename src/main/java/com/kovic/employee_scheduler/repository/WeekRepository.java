package com.kovic.employee_scheduler.repository;

import com.kovic.employee_scheduler.model.Week;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WeekRepository extends JpaRepository<Week, Long> {
    Optional<Week> findByYearAndWeekNumber(int year, int weekNumber);
}
