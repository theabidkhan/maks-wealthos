CREATE TABLE wealthos.audit_log
(
    audit_id BIGSERIAL PRIMARY KEY,

    entity_name VARCHAR(100),

    entity_id BIGINT,

    operation_type VARCHAR(20),

    before_data JSONB,

    after_data JSONB,

    changed_by VARCHAR(100),

    changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_audit_entity
    ON wealthos.audit_log(entity_name, entity_id);

CREATE INDEX idx_audit_changed_at
    ON wealthos.audit_log(changed_at);