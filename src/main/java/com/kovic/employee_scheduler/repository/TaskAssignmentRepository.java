package com.kovic.employee_scheduler.repository;

import com.kovic.employee_scheduler.model.TaskAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskAssignmentRepository extends JpaRepository<TaskAssignment, Long> {
}
