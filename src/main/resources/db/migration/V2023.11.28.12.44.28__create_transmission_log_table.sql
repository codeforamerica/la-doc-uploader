CREATE TYPE transmission_status AS ENUM('Queued', 'Failed', 'Complete');
CREATE TYPE transmission_type AS ENUM('SNAP', 'ECE', 'WIC');
-- CREATE TYPE submission_error_type AS ENUM('INCOMPLETE_RECORD', 'OTHER');
-- CREATE TYPE documentation_error_type AS ENUM('NOT_FOUND', 'PERMISSION_DENIED');
CREATE TABLE transmissions (
    transmission_id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    submission_id uuid REFERENCES submissions(id),
    batch_id integer,
    run_id uuid,
    time_sent TIMESTAMP WITHOUT TIME ZONE,
    time_created TIMESTAMP WITHOUT TIME ZONE DEFAULT now(),
    status transmission_status DEFAULT 'Queued',
    transmission_type transmission_type,
    submission_errors jsonb
);

CREATE INDEX idx_transmission_id on transmissions (batch_id, submission_id);
