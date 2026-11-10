CREATE TABLE wealthos.investment_transactions
(
    investment_transaction_id BIGSERIAL PRIMARY KEY,

    investment_id BIGINT NOT NULL,

    transaction_date DATE NOT NULL,

    transaction_type VARCHAR(30),

    amount NUMERIC(15,2),

    units NUMERIC(15,4),

    nav NUMERIC(15,4),

    CONSTRAINT fk_inv_tx
        FOREIGN KEY(investment_id)
            REFERENCES wealthos.investments(investment_id)
);

CREATE INDEX idx_inv_tx_id
    ON wealthos.investment_transactions(investment_id);

CREATE INDEX idx_inv_tx_date
    ON wealthos.investment_transactions(transaction_date);