package com.kovic.employee_scheduler.repository;

import com.kovic.employee_scheduler.model.TaskAssignment;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.UUID;

public interface TaskAssignmentRepository extends JpaRepository<TaskAssignment, Long> {
}
