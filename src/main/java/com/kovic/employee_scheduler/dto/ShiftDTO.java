package com.kovic.employee_scheduler.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * A DTO of shift object, isolating only the specific date, the time the shift starts and ends.
 */
@Data
public class ShiftDTO {
    private LocalDate actualDate;
    private LocalTime startsAt;
    private LocalTime endsAt;
}
