CREATE TABLE IF NOT EXISTS orders(
    id SERIAL NOT NULL,
    customer_id UUID NOT NULL,
    created_time timestamp NOT NULL,
    updated_time timestamp NOT NULL,
    version integer,
    PRIMARY KEY (id)
);