CREATE TABLE menu_items (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price NUMERIC(10,2) NOT NULL,
    available BOOLEAN DEFAULT true,
    restaurant_id BIGINT NOT NULL REFERENCES restaurants(id)
);