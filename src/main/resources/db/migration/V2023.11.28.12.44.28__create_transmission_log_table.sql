CREATE TYPE transmission_status AS ENUM('Failed', 'Complete');
CREATE TYPE transmission_type AS ENUM('ECE_WIC_csv', 'Application');
CREATE TABLE transmissions (
    transmission_id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    submission_id uuid REFERENCES submissions(id),
    batch_id uuid,
    time_sent TIMESTAMP WITHOUT TIME ZONE,
    status transmission_status,
    transmission_type transmission_type
);

CREATE INDEX idx_transmission_id on transmissions (batch_id, submission_id);
