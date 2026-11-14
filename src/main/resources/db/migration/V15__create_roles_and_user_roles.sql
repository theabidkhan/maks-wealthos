-- Drop foreign key constraints referencing the old users table
ALTER TABLE wealthos.transactions DROP CONSTRAINT IF EXISTS fk_transaction_user;
ALTER TABLE wealthos.budgets DROP CONSTRAINT IF EXISTS fk_budget_user;
ALTER TABLE wealthos.loans DROP CONSTRAINT IF EXISTS fk_loan_user;
ALTER TABLE wealthos.insurance_policies DROP CONSTRAINT IF EXISTS fk_policy_user;
ALTER TABLE wealthos.investments DROP CONSTRAINT IF EXISTS fk_investment_user;
ALTER TABLE wealthos.financial_goals DROP CONSTRAINT IF EXISTS fk_goal_user;

-- Update users table to match UserEntity
ALTER TABLE wealthos.users
    RENAME TO users_old_backup;

CREATE TABLE wealthos.users
(
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    mobile_number VARCHAR(20) UNIQUE,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

-- Create roles table
CREATE TABLE IF NOT EXISTS wealthos.roles
(
    id BIGSERIAL PRIMARY KEY,
    role_name VARCHAR(50) NOT NULL UNIQUE
);

-- Create user_roles join table
CREATE TABLE IF NOT EXISTS wealthos.user_roles
(
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_roles_user
        FOREIGN KEY (user_id) REFERENCES wealthos.users(id) ON DELETE CASCADE,
    CONSTRAINT fk_user_roles_role
        FOREIGN KEY (role_id) REFERENCES wealthos.roles(id) ON DELETE CASCADE
);

-- Migrate existing data (if any)
INSERT INTO wealthos.users (first_name, last_name, email, password, mobile_number, active, created_at, updated_at)
SELECT
    COALESCE(SPLIT_PART(username, ' ', 1), 'Unknown'),
    COALESCE(SUBSTRING(username FROM POSITION(' ' IN username) + 1), 'Unknown'),
    email,
    '$2a$10$dummyhashfor migration',
    mobile_number,
    CASE WHEN status = 'ACTIVE' THEN TRUE ELSE FALSE END,
    created_at,
    updated_at
FROM wealthos.users_old_backup
ON CONFLICT (email) DO NOTHING;

-- Drop backup table
DROP TABLE IF EXISTS wealthos.users_old_backup CASCADE;

-- Re-create foreign key constraints referencing the new users table
ALTER TABLE wealthos.transactions
    ADD CONSTRAINT fk_transaction_user
        FOREIGN KEY (user_id) REFERENCES wealthos.users(id);

ALTER TABLE wealthos.budgets
    ADD CONSTRAINT fk_budget_user
        FOREIGN KEY (user_id) REFERENCES wealthos.users(id);

ALTER TABLE wealthos.loans
    ADD CONSTRAINT fk_loan_user
        FOREIGN KEY (user_id) REFERENCES wealthos.users(id);

ALTER TABLE wealthos.insurance_policies
    ADD CONSTRAINT fk_policy_user
        FOREIGN KEY (user_id) REFERENCES wealthos.users(id);

ALTER TABLE wealthos.investments
    ADD CONSTRAINT fk_investment_user
        FOREIGN KEY (user_id) REFERENCES wealthos.users(id);

ALTER TABLE wealthos.financial_goals
    ADD CONSTRAINT fk_goal_user
        FOREIGN KEY (user_id) REFERENCES wealthos.users(id);

-- Seed default roles
INSERT INTO wealthos.roles (role_name) VALUES
    ('USER'),
    ('ADMIN')
ON CONFLICT (role_name) DO NOTHING;

-- Create indexes
CREATE INDEX IF NOT EXISTS idx_roles_role_name ON wealthos.roles(role_name);
CREATE INDEX IF NOT EXISTS idx_user_roles_user_id ON wealthos.user_roles(user_id);
CREATE INDEX IF NOT EXISTS idx_user_roles_role_id ON wealthos.user_roles(role_id);
CREATE INDEX IF NOT EXISTS idx_users_email ON wealthos.users(email);
CREATE INDEX IF NOT EXISTS idx_users_active ON wealthos.users(active);
