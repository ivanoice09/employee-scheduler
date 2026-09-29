package com.kovic.employee_scheduler.repository;

import com.kovic.employee_scheduler.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
