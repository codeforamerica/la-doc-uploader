CREATE TABLE IF NOT EXISTS user_files
 (
     file_id SERIAL PRIMARY KEY ,
     submission_id INT REFERENCES submissions(id),
     created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
     original_name VARCHAR NOT NULL,
     repository_path VARCHAR NOT NULL,
     extension VARCHAR NOT NULL,
     filesize REAL NOT NULL
 );

CREATE INDEX idx_submission_id on user_files (submission_id);
