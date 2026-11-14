CREATE TABLE wealthos.users
(
    id BIGSERIAL PRIMARY KEY,

    first_name VARCHAR(100) NOT NULL,

    last_name VARCHAR(100) NOT NULL,

    email VARCHAR(255) NOT NULL,

    password VARCHAR(255) NOT NULL,

    mobile_number VARCHAR(20),

    active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP,

    updated_at TIMESTAMP
);

ALTER TABLE wealthos.users
    ADD CONSTRAINT uk_users_email UNIQUE(email);

CREATE INDEX idx_users_active
    ON wealthos.users(active);