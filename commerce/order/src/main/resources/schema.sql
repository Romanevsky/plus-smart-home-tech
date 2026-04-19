CREATE TABLE IF NOT EXISTS addresses (
    address_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    country varchar(50),
    city varchar(50),
    street varchar(50),
    house varchar(50),
    flat varchar(50)
);

CREATE TABLE IF NOT EXISTS orders (
    order_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    username varchar(50),
    shopping_cart_id UUID,
    address_id UUID REFERENCES addresses (address_id),
    payment_id UUID,
    delivery_id UUID,
    state varchar(50),
    delivery_weight float8,
    delivery_volume float8,
    fragile bool,
    total_price numeric,
    delivery_price numeric,
    product_price numeric
);


CREATE TABLE IF NOT EXISTS order_product_mapping (
    order_id UUID,
    product_id UUID,
    quantity int,
    PRIMARY KEY (order_id, product_id)
);



