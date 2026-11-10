CREATE TABLE wealthos.investments
(
    investment_id BIGSERIAL PRIMARY KEY,

    user_id BIGINT NOT NULL,

    investment_name VARCHAR(200),

    investment_type VARCHAR(50),

    invested_amount NUMERIC(15,2),

    current_value NUMERIC(15,2),

    units NUMERIC(15,4),

    status VARCHAR(20),

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_investment_user
        FOREIGN KEY(user_id)
            REFERENCES wealthos.users(user_id)
);

CREATE INDEX idx_investment_type
    ON wealthos.investments(investment_type);

CREATE INDEX idx_investment_user
    ON wealthos.investments(user_id);