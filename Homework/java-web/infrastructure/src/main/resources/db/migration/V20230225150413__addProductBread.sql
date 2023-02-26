INSERT INTO product (id, uuid, name, price, created_at)
VALUES (1, gen_random_uuid(), 'Bread', 20, now()::timestamptz);