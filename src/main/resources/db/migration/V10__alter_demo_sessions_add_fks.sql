-- rename demo_sessions pk from "session_id" to "demo_session_id" for clarity
ALTER TABLE demo_sessions
RENAME COLUMN session_id to demo_session_id;

-- now make every demo_session_id a foreign key
ALTER TABLE weeks
    ADD CONSTRAINT fk_weeks_demo_session
        FOREIGN KEY (demo_session_id)
            REFERENCES demo_sessions(demo_session_id)
            ON DELETE CASCADE;

ALTER TABLE shifts
    ADD CONSTRAINT fk_shifts_demo_session
        FOREIGN KEY (demo_session_id)
            REFERENCES demo_sessions(demo_session_id)
            ON DELETE CASCADE;

ALTER TABLE task_assignments
ADD CONSTRAINT fk_task_assignments_demo_session
        FOREIGN KEY (demo_session_id)
            REFERENCES demo_sessions(demo_session_id)
            ON DELETE CASCADE;


