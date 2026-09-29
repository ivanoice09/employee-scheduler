package com.kovic.employee_scheduler.repository;

import com.kovic.employee_scheduler.model.DemoSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DemoSessionRepository extends JpaRepository<DemoSession, UUID> {

    Optional<DemoSession> findBySessionId(UUID sessionId);

    @Modifying
    @Query("DELETE FROM DemoSession ds WHERE ds.lastActiveAt < :cutoff")
    int deleteAllByLastActiveAtBefore(OffsetDateTime cutoff);
}
