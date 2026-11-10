CREATE TABLE wealthos.insurance_policies
(
    policy_id BIGSERIAL PRIMARY KEY,

    user_id BIGINT NOT NULL,

    company_name VARCHAR(150),

    policy_name VARCHAR(200),

    premium_amount NUMERIC(15,2),

    payment_frequency VARCHAR(20),

    start_date DATE,

    maturity_date DATE,

    policy_status VARCHAR(20),

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_policy_user
        FOREIGN KEY(user_id)
            REFERENCES wealthos.users(user_id)
);

CREATE INDEX idx_policy_status
    ON wealthos.insurance_policies(policy_status);

CREATE INDEX idx_policy_user
    ON wealthos.insurance_policies(user_id);