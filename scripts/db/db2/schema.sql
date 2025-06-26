-- DB2-compatible schema for API Orchestrator

-- =========================================
-- job_groups table
-- =========================================
CREATE TABLE job_groups (
    name VARCHAR(100) NOT NULL PRIMARY KEY,
    color_tag VARCHAR(50) NOT NULL
);

-- =========================================
-- jobs table
-- =========================================
CREATE TABLE jobs (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description CLOB,
    endpoint VARCHAR(255) NOT NULL,
    cron_expression VARCHAR(100) NOT NULL,
    group_tag VARCHAR(100),
    owner VARCHAR(100),
    is_active SMALLINT NOT NULL DEFAULT 1,
    last_run_successful SMALLINT NOT NULL DEFAULT 1,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_group_tag ON jobs (group_tag);
CREATE INDEX idx_owner ON jobs (owner);

-- =========================================
-- job_logs table
-- =========================================
CREATE TABLE job_logs (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    job_id BIGINT NOT NULL,
    run_timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(50),
    response_code INTEGER,
    response_body CLOB,
    error_message CLOB,

    CONSTRAINT fk_job FOREIGN KEY (job_id) REFERENCES jobs(id)
);

CREATE INDEX idx_job_id ON job_logs (job_id);
