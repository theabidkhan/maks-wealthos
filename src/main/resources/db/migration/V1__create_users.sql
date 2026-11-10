CREATE TABLE wealthos.users
(
    user_id BIGSERIAL PRIMARY KEY,

    username VARCHAR(100) NOT NULL,

    email VARCHAR(255) NOT NULL,

    mobile_number VARCHAR(20),

    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE wealthos.users
    ADD CONSTRAINT uk_users_email UNIQUE(email);

CREATE INDEX idx_users_status
    ON wealthos.users(status);