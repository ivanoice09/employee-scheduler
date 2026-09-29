ALTER TABLE weeks
    DROP CONSTRAINT weeks_year_week_number_key;

ALTER TABLE weeks
    ADD CONSTRAINT weeks_session_year_week_unique
    UNIQUE (demo_session_id, year, week_number);