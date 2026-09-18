CREATE TABLE incident_evidence (
    id BIGSERIAL PRIMARY KEY,

    incident_id BIGINT NOT NULL,

    type VARCHAR(30) NOT NULL,

    source VARCHAR(100) NOT NULL,

    content TEXT NOT NULL,

    observed_at TIMESTAMP WITH TIME ZONE NOT NULL,

    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_incident_evidence_incident
        FOREIGN KEY (incident_id)
        REFERENCES incidents(id)
);