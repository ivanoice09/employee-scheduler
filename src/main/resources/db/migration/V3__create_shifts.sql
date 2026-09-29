CREATE TABLE shifts (
    shift_id BIGSERIAL PRIMARY KEY,
    actual_date DATE,
    starts_at TIME NOT NULL,
    ends_at TIME NOT NULL,

    -- many shifts belong to 1 employee
    employee_id BIGINT NOT NULL REFERENCES employees(employee_id),

    -- many shifts belong/are recorded to 1 week instance
    week_id BIGINT NOT NULL REFERENCES weeks(week_id),

    CONSTRAINT chk_shifts_end_after_start CHECK (ends_at > starts_at)
);