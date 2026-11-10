CREATE TABLE wealthos.budgets
(
    budget_id BIGSERIAL PRIMARY KEY,

    user_id BIGINT NOT NULL,

    category_id BIGINT NOT NULL,

    budget_year INTEGER NOT NULL,

    budget_month INTEGER NOT NULL,

    planned_amount NUMERIC(15,2),

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_budget_user
        FOREIGN KEY(user_id)
            REFERENCES wealthos.users(user_id),

    CONSTRAINT fk_budget_category
        FOREIGN KEY(category_id)
            REFERENCES wealthos.categories(category_id)
);

CREATE UNIQUE INDEX uk_budget_month
    ON wealthos.budgets(user_id, category_id, budget_year, budget_month);