ALTER TABLE user_files ADD COLUMN rpa_status rpa_status_type;
ALTER TABLE user_files ADD COLUMN rpa_status_description VARCHAR(50);
ALTER TABLE user_files ADD COLUMN rpa_critical_failure BOOLEAN DEFAULT false;