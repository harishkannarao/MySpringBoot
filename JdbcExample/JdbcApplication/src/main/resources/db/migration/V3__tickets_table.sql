CREATE TABLE IF NOT EXISTS tickets(
    id UUID PRIMARY KEY,
    status VARCHAR(255) NOT NULL DEFAULT 'AVAILABLE',
    customer_id UUID,
    updated_time timestamp NOT NULL
);