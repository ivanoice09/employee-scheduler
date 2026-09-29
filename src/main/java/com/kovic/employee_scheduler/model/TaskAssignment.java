package com.kovic.employee_scheduler.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Entity
@Table(name = "task_assignments")
@Getter
@Setter
public class TaskAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_assignment_id")
    private Long id;

    @Column(name = "task_starts_at", nullable = false)
    private LocalTime taskStartsAt;

    @Column(name = "task_ends_at", nullable = false)
    private LocalTime taskEndsAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shift_id", nullable = false)
    private Shift shift;
}
