ALTER TYPE rpa_status_type ADD VALUE 'CriticalFailure';
ALTER TABLE user_files DROP COLUMN rpa_critical_failure;
ALTER TABLE user_files ADD COLUMN rpa_process_start_time DATE;
ALTER TABLE user_files ADD COLUMN rpa_process_end_time DATE;