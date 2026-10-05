package com.kovic.employee_scheduler.repository;

import com.kovic.employee_scheduler.model.DemoSession;
import com.kovic.employee_scheduler.model.Week;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface WeekRepository extends JpaRepository<Week, Long> {

    Optional<Week> findByYearAndWeekNumberAndDemoSessionId(
            int year,
            int weekNumber,
            UUID demoSessionId
    );

    @Modifying
    @Query("delete from Week w where w.demoSession.id = :sessionId")
    void deleteByDemoSessionId(UUID sessionId);
}
