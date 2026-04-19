CREATE TABLE IF NOT EXISTS addresses (
    address_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    country varchar(50),
    city varchar(50),
    street varchar(50),
    house varchar(50),
    flat varchar(50)
);

CREATE TABLE IF NOT EXISTS deliveries (
    delivery_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    from_address_id UUID REFERENCES addresses (address_id),
    to_address_id UUID REFERENCES addresses (address_id),
    order_id UUID,
    delivery_state varchar(50)
);



