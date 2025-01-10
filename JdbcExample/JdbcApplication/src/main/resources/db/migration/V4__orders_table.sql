CREATE TABLE IF NOT EXISTS orders(
    id SERIAL,
    customer_id UUID,
    created_time timestamp NOT NULL,
    updated_time timestamp NOT NULL,
    version integer
);