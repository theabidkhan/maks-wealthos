CREATE TABLE wealthos.transactions
(
    transaction_id BIGSERIAL PRIMARY KEY,

    user_id BIGINT NOT NULL,

    category_id BIGINT NOT NULL,

    transaction_date DATE NOT NULL,

    transaction_type VARCHAR(20) NOT NULL,

    amount NUMERIC(15,2) NOT NULL,

    description VARCHAR(500),

    account_name VARCHAR(100),

    reference_number VARCHAR(100),

    remarks VARCHAR(1000),

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_transaction_user
        FOREIGN KEY(user_id)
            REFERENCES wealthos.users(user_id),

    CONSTRAINT fk_transaction_category
        FOREIGN KEY(category_id)
            REFERENCES wealthos.categories(category_id)
);

CREATE INDEX idx_transactions_date
    ON wealthos.transactions(transaction_date);

CREATE INDEX idx_transactions_type
    ON wealthos.transactions(transaction_type);

CREATE INDEX idx_transactions_category
    ON wealthos.transactions(category_id);

CREATE INDEX idx_transactions_user
    ON wealthos.transactions(user_id);

CREATE INDEX idx_transactions_monthly
    ON wealthos.transactions(user_id, transaction_date, transaction_type);