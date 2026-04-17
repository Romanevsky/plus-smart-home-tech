CREATE TABLE IF NOT EXISTS shopping_store_products (
    product_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    product_name varchar(100),
    description varchar(1000),
    image_src varchar(100),
    quantity_state varchar(20),
    product_state varchar(20),
    product_category varchar(20),
    price numeric
);



