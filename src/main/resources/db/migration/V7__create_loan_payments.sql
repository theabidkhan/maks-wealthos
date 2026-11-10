CREATE TABLE wealthos.loan_payments
(
    payment_id BIGSERIAL PRIMARY KEY,

    loan_id BIGINT NOT NULL,

    payment_date DATE NOT NULL,

    amount_paid NUMERIC(15,2),

    principal_component NUMERIC(15,2),

    interest_component NUMERIC(15,2),

    remarks VARCHAR(500),

    CONSTRAINT fk_loan_payment_loan
        FOREIGN KEY(loan_id)
            REFERENCES wealthos.loans(loan_id)
);

CREATE INDEX idx_loan_payment_loan
    ON wealthos.loan_payments(loan_id);

CREATE INDEX idx_loan_payment_date
    ON wealthos.loan_payments(payment_date);