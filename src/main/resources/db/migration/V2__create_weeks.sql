CREATE TABLE weeks (
    week_id BIGSERIAL PRIMARY KEY,
    year INTEGER NOT NULL,
    week_number INTEGER NOT NULL,
    week_start_date DATE,
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT chk_weeks_status CHECK (status IN ('DRAFT', 'PUBLISHED', 'LOCKED')),
    UNIQUE(year, week_number)
);