package com.kovic.employee_scheduler.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "employees")
@Getter
@Setter
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id")
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "tax_code", columnDefinition = "CHAR(16)", length = 16)
    @JdbcTypeCode(java.sql.Types.CHAR)
    private String taxCode;

    @Column(name = "address")
    private String address;

    @Column(name = "city")
    private String city;

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
    protected void onUpdate() {
        updatedAt = OffsetDateTime.now();
    }
}
