CREATE TYPE rpa_status_type AS ENUM('NotReady', 'Ready', 'InProgress', 'Failed', 'Complete');
ALTER TABLE submissions ADD COLUMN rpa_status rpa_status_type;