CREATE TABLE IF NOT EXISTS order_documents(
    id UUID NOT NULL,
    order_id int NOT NULL,
    data jsonb,
    version integer,
    PRIMARY KEY (id)
);

ALTER TABLE order_documents
    ADD CONSTRAINT fk_order FOREIGN KEY (order_id) REFERENCES orders(id);