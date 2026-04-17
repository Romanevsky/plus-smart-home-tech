CREATE TABLE IF NOT EXISTS payments (
    payment_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    order_id UUID,
    total_payment numeric,
    delivery_total numeric,
    fee_total numeric,
    status varchar(50)
);



