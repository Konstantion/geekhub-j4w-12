INSERT INTO product (uuid, name, price, created_at)
VALUES (gen_random_uuid(), 'Bread', 20, now()::timestamptz);