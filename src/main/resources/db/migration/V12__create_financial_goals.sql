CREATE TABLE wealthos.financial_goals
(
    goal_id BIGSERIAL PRIMARY KEY,

    user_id BIGINT NOT NULL,

    goal_name VARCHAR(200),

    target_amount NUMERIC(15,2),

    current_amount NUMERIC(15,2),

    target_date DATE,

    goal_status VARCHAR(20),

    CONSTRAINT fk_goal_user
        FOREIGN KEY(user_id)
            REFERENCES wealthos.users(user_id)
);

CREATE INDEX idx_goal_user
    ON wealthos.financial_goals(user_id);