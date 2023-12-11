CREATE TABLE transmissions (
    transmission_id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    submission_id uuid REFERENCES submissions(id),
    run_id uuid,
    time_sent TIMESTAMP WITHOUT TIME ZONE,
    time_created TIMESTAMP WITHOUT TIME ZONE DEFAULT now(),
    status varchar DEFAULT 'Queued',
    transmission_type varchar,
    submission_errors JSONB
);

CREATE INDEX idx_transmission_id on transmissions (run_id, submission_id);
