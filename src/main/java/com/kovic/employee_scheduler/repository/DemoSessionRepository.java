package com.kovic.employee_scheduler.repository;

import com.kovic.employee_scheduler.model.DemoSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface DemoSessionRepository extends JpaRepository<DemoSession, UUID> {
    List<DemoSession> findByLastActiveAtBefore(OffsetDateTime lastActiveAtBefore);
}
