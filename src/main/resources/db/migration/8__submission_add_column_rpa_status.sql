CREATE TYPE rpa_status_type AS ENUM('in_progress', 'failed', 'succeeded');
ALTER TABLE submissions ADD COLUMN rpa_status rpa_status_type;