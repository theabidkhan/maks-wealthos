CREATE TABLE wealthos.categories
(
    category_id BIGSERIAL PRIMARY KEY,

    category_name VARCHAR(100) NOT NULL,

    category_type VARCHAR(20) NOT NULL,

    display_order INTEGER DEFAULT 0,

    active_flag BOOLEAN DEFAULT TRUE,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX uk_categories_name
    ON wealthos.categories(category_name);

CREATE INDEX idx_categories_type
    ON wealthos.categories(category_type);