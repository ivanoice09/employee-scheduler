ALTER TABLE weeks
    ADD COLUMN demo_session_id UUID;

ALTER TABLE shifts
    ADD COLUMN demo_session_id UUID;

ALTER TABLE task_assignments
    ADD COLUMN demo_session_id UUID;

-- Implement in the future:
-- ALTER TABLE employees
--     ADD COLUMN demo_session_id UUID;

-- Indexes to keep queries fast:
CREATE INDEX idx_weeks_demo_session_id ON weeks(demo_session_id);
CREATE INDEX idx_shifts_demo_session_id ON shifts(demo_session_id);
CREATE INDEX idx_task_assignments_demo_session_id ON task_assignments(demo_session_id);