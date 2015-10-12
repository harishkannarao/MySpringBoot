DROP TABLE IF EXISTS customers CASCADE;
CREATE TABLE customers(
    id SERIAL,
    first_name VARCHAR(255),
    last_name VARCHAR(255)
);
DROP TABLE IF EXISTS menu_entries CASCADE;
CREATE TABLE menu_entries(
    id SERIAL,
    entry VARCHAR(255)
);
