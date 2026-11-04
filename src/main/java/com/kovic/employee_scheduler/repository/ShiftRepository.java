package com.kovic.employee_scheduler.repository;

import com.kovic.employee_scheduler.model.DemoSession;
import com.kovic.employee_scheduler.model.Employee;
import com.kovic.employee_scheduler.model.Shift;
import com.kovic.employee_scheduler.model.Week;
import org.springframework.data.jpa.repository.JpaRepository;

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

    Optional<Shift> findByIdAndDemoSessionId(Long shiftId, UUID demoSessionId);
}
