CREATE TABLE IF NOT EXISTS shopping_carts (
    shopping_cart_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    username varchar(50),
    active bool
);

CREATE TABLE IF NOT EXISTS cart_product_mapping (
    shopping_cart_id UUID,
    product_id UUID,
    quantity int,
    PRIMARY KEY (shopping_cart_id, product_id)
);



