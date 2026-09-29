package com.kovic.employee_scheduler.repository;

import com.kovic.employee_scheduler.model.Employee;
import com.kovic.employee_scheduler.model.Shift;
import com.kovic.employee_scheduler.model.Week;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ShiftRepository extends JpaRepository<Shift, Long> {
    Optional<Shift> findByWeekAndEmployeeAndActualDate(Week week, Employee employee, LocalDate actualDate);
    List<Shift> findByWeekId(Long weekId);
}
