CREATE TABLE IF NOT EXISTS warehouse_products (
    product_id UUID PRIMARY KEY,
    fragile bool,
    width float8,
    height float8,
    depth float8,
    weight float8,
    quantity bigint
);



