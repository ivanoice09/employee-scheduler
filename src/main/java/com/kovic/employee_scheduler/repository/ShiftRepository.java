package com.kovic.employee_scheduler.repository;

import com.kovic.employee_scheduler.model.DemoSession;
import com.kovic.employee_scheduler.model.Employee;
import com.kovic.employee_scheduler.model.Shift;
import com.kovic.employee_scheduler.model.Week;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ShiftRepository extends JpaRepository<Shift, Long> {

    List<Shift> findByWeekIdAndDemoSessionId(Long weekId, UUID demoSessionId);

    Optional<Shift> findByWeekAndEmployeeAndActualDateAndDemoSession(
            Week week,
            Employee employee,
            LocalDate actualDate,
            DemoSession demoSession
    );

    @Modifying
    @Query("delete from Shift s where s.demoSession.id = :demoSessionId")
    void deleteByDemoSessionId(UUID demoSessionId);
}
