package com.kovic.employee_scheduler.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeDTO {
    private Long employeeId;
    private String firstName;
    private String middleName;
    private String lastName;
}
