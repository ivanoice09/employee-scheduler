package com.kovic.employee_scheduler.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "demo_sessions")
@Getter
@Setter
public class DemoSession {

    @Id
    @Column(name = "session_id")
    private UUID sessionId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "last_active_at", nullable = false)
    private OffsetDateTime lastActiveAt;

    @PreUpdate
    protected void onUpdate() {
        lastActiveAt = OffsetDateTime.now();
    }
}
