CREATE TABLE wealthos.loans
(
    loan_id BIGSERIAL PRIMARY KEY,

    user_id BIGINT NOT NULL,

    loan_name VARCHAR(150),

    lender_name VARCHAR(150),

    loan_type VARCHAR(50),

    loan_amount NUMERIC(15,2),

    outstanding_amount NUMERIC(15,2),

    roi NUMERIC(5,2),

    emi NUMERIC(15,2),

    tenure_months INTEGER,

    start_date DATE,

    end_date DATE,

    status VARCHAR(20),

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_loan_user
        FOREIGN KEY(user_id)
            REFERENCES wealthos.users(user_id)
);

CREATE INDEX idx_loans_status
    ON wealthos.loans(status);

CREATE INDEX idx_loans_user
    ON wealthos.loans(user_id);