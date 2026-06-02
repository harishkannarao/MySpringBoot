CREATE TABLE IF NOT EXISTS orders(
    id SERIAL NOT NULL,
    customer_id UUID NOT NULL,
    created_time TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_time TIMESTAMP WITH TIME ZONE NOT NULL,
    version integer,
    PRIMARY KEY (id)
);