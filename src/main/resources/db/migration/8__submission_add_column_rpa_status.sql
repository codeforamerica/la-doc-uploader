CREATE TYPE rpa_status_type AS ENUM('submission_ready', 'in_progress', 'failed', 'success');
ALTER TABLE submissions ADD COLUMN rpa_status rpa_status_type;