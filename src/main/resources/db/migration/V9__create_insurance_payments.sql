CREATE TABLE wealthos.insurance_payments
(
    insurance_payment_id BIGSERIAL PRIMARY KEY,

    policy_id BIGINT NOT NULL,

    payment_date DATE,

    amount NUMERIC(15,2),

    remarks VARCHAR(500),

    CONSTRAINT fk_insurance_payment_policy
        FOREIGN KEY(policy_id)
            REFERENCES wealthos.insurance_policies(policy_id)
);

CREATE INDEX idx_policy_payment_policy
    ON wealthos.insurance_payments(policy_id);