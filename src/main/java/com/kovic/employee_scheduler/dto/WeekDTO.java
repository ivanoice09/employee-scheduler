package com.kovic.employee_scheduler.dto;

import com.kovic.employee_scheduler.model.Week;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * WeekDTO is an object of "everything".
 * - details of a SAVED week instance.
 * - assignments of employees.
 */
@Data
public class WeekDTO {
    private int year;
    private int weekNumber;
    private LocalDate startDate;
    private Week.Status status;
    private List<EmployeeDTO> employees;
    private List<ShiftAssignmentDTO> assignments;
    private boolean existingWeek;
}
