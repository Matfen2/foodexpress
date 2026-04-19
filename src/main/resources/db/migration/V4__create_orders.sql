CREATE TABLE customer_orders (
    id BIGSERIAL PRIMARY KEY,
    status VARCHAR(20) NOT NULL,
    customer_id BIGINT NOT NULL REFERENCES customers(id),
    restaurant_id BIGINT NOT NULL REFERENCES restaurants(id),
    total_amount NUMERIC(10,2) NOT NULL,
    placed_at TIMESTAMP,
    delivered_at TIMESTAMP
);

CREATE TABLE order_items (
    id BIGSERIAL PRIMARY KEY,
    quantity INT NOT NULL,
    unit_price NUMERIC(10,2) NOT NULL,
    subtotal NUMERIC(10,2) NOT NULL,
    order_id BIGINT NOT NULL REFERENCES customer_orders(id),
    menu_item_id BIGINT NOT NULL REFERENCES menu_items(id)
);