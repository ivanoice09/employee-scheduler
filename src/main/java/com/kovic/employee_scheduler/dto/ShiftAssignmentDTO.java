package com.kovic.employee_scheduler.dto;

import lombok.Data;

import java.util.List;

/**
 * Assignment of shifts to employees: employee ID and shifts pair.
 */
@Data
public class ShiftAssignmentDTO {
    private Long employeeId;
    private List<ShiftDTO> shifts;
}
