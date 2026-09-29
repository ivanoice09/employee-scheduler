package com.kovic.employee_scheduler.dto;

import com.kovic.employee_scheduler.model.Week;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * Specific DTO for saving an instance of WeekDTO into the database.
 */
@Data
public class SaveWeekDTO {
    private int year;
    private int weekNumber;
    private LocalDate weekStartDate;
    private Week.Status status;
    private List<ShiftAssignmentDTO> assignments;
}
