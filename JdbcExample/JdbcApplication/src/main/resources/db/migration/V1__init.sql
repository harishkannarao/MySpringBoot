CREATE TABLE IF NOT EXISTS customers(
    id SERIAL,
    first_name VARCHAR(255),
    last_name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS menu_entries(
    id SERIAL,
    entry VARCHAR(255)
);