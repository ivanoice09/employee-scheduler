package com.kovic.employee_scheduler.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(
        name = "weeks",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {
                        "year",
                        "week_number"
                }
        )
)
@Getter
@Setter
public class Week {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "week_id")
    private Long id;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Column(name = "week_number", nullable = false)
    private Integer weekNumber;

    @Column(name = "week_start_date")
    private LocalDate weekStartDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "created_at", columnDefinition = "TIMESTAMPTZ", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMPTZ", nullable = false)
    private OffsetDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now();
        updatedAt = OffsetDateTime.now();
    }

    @PreUpdate
    protected void onUpdated() {
        updatedAt = OffsetDateTime.now();
    }

    public enum Status {
        DRAFT, PUBLISHED, LOCKED
    }
}
