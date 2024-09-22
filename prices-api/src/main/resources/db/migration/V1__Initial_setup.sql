CREATE TABLE prices (
    id BIGINT PRIMARY KEY CHECK (id >= 0),
    product_id BIGINT NOT NULL CHECK (product_id >= 0),
    brand_id BIGINT NOT NULL CHECK (brand_id >= 0),
    price_list_id BIGINT NOT NULL CHECK (price_list_id >= 0),
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    priority INT NOT NULL,
    currency VARCHAR(3) NOT NULL
);