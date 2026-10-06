ALTER TABLE task_assignments
DROP CONSTRAINT IF EXISTS fk_task_assignments_demo_session,
    ADD CONSTRAINT fk_task_assignments_demo_session
    FOREIGN KEY (demo_session_id)
    REFERENCES demo_sessions(demo_session_id)
    ON DELETE CASCADE;

ALTER TABLE shifts
DROP CONSTRAINT IF EXISTS fk_shifts_demo_session,
    ADD CONSTRAINT fk_shifts_demo_session
    FOREIGN KEY (demo_session_id)
    REFERENCES demo_sessions(demo_session_id)
    ON DELETE CASCADE;

ALTER TABLE weeks
DROP CONSTRAINT IF EXISTS fk_weeks_demo_session,
    ADD CONSTRAINT fk_weeks_demo_session
    FOREIGN KEY (demo_session_id)
    REFERENCES demo_sessions(demo_session_id)
    ON DELETE CASCADE;