package com.kovic.employee_scheduler.service;

import com.kovic.employee_scheduler.dto.EmployeeDTO;
import com.kovic.employee_scheduler.model.Employee;
import com.kovic.employee_scheduler.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<EmployeeDTO> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();

        return employees.stream()
                .map(employee -> {
                    EmployeeDTO employeeDTO = new EmployeeDTO();
                    employeeDTO.setEmployeeId(employee.getId());
                    employeeDTO.setFirstName(employee.getFirstName());
                    employeeDTO.setMiddleName(employee.getMiddleName());
                    employeeDTO.setLastName(employee.getLastName());

                    return employeeDTO;
                }).toList();
    }
}
