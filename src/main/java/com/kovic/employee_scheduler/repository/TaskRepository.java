package com.kovic.employee_scheduler.repository;

import com.kovic.employee_scheduler.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
