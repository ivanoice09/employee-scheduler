CREATE TABLE task_assignments (
    task_assignment_id BIGSERIAL PRIMARY KEY,
    task_starts_at TIME NOT NULL,
    task_ends_at TIME NOT NULL,

    -- many task assignments can be associated to 1 task
    task_id BIGINT NOT NULL REFERENCES tasks(task_id) ON DELETE CASCADE,

    -- many task assignments can be associated to 1 shift. But obviously, depending how many hours an employee works on that particular week,
    -- limitations will be dealt by the function service
    shift_id BIGINT NOT NULL REFERENCES shifts(shift_id) ON DELETE CASCADE,
    CONSTRAINT chk_task_assignments_end_after_start CHECK (task_ends_at > task_starts_at)
);
