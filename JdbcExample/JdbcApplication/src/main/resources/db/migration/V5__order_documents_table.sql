CREATE TABLE IF NOT EXISTS order_documents(
    id UUID NOT NULL,
    order_id int NOT NULL,
    json_data jsonb,
    inventory jsonb,
    PRIMARY KEY (id)
);

CREATE UNIQUE INDEX unique_index_order_id ON order_documents (order_id);

ALTER TABLE order_documents
    ADD CONSTRAINT fk_order FOREIGN KEY (order_id) REFERENCES orders(id);