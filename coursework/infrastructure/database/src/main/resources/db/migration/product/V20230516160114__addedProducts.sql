INSERT INTO public.product (name, price, weight, category_id, image_bytes, description, creator_id, created_at, deactivate_at, active)
VALUES
    ('Spinach Artichoke Dip', 10.99, 200, '2deb444a-fdc4-4949-943e-3fb74a691380', null, null, null, now()::timestamptz, null, true),
    ('Chicken Wings', 12.99, 180, '2deb444a-fdc4-4949-943e-3fb74a691380', null, null, null, now()::timestamptz, null, true),
    ('Mozzarella Sticks', 9.99, 160, '2deb444a-fdc4-4949-943e-3fb74a691380', null, null, null, now()::timestamptz, null, true);


INSERT INTO public.product (name, price, weight, category_id, image_bytes, description, creator_id, created_at, deactivate_at, active)
VALUES
    ('Ribeye Steak', 24.99, 300, 'd988f011-15c1-43eb-b855-7b5da1aaa98d', null, null, null, now()::timestamptz, null, true),
    ('Filet Mignon', 29.99, 250, 'd988f011-15c1-43eb-b855-7b5da1aaa98d', null, null, null, now()::timestamptz, null, true),
    ('T-Bone Steak', 22.99, 280, 'd988f011-15c1-43eb-b855-7b5da1aaa98d', null, null, null, now()::timestamptz, null, true);


INSERT INTO public.product (name, price, weight, category_id, image_bytes, description, creator_id, created_at, deactivate_at, active)
VALUES
    ('Tomato Basil Soup', 7.99, 150, 'fd6ca92a-7020-4594-921c-bd3da9b68bbf', null, null, null, now()::timestamptz, null, true),
    ('Chicken Noodle Soup', 6.99, 170, 'fd6ca92a-7020-4594-921c-bd3da9b68bbf', null, null, null, now()::timestamptz, null, true),
    ('Lentil Soup', 5.99, 160, 'fd6ca92a-7020-4594-921c-bd3da9b68bbf', null, null, null, now()::timestamptz, null, true);


INSERT INTO public.product (name, price, weight, category_id, image_bytes, description, creator_id, created_at, deactivate_at, active)
VALUES
    ('Caesar Salad', 8.99, 120, 'ebc3892c-c0db-48e0-8329-a4256bb52947', null, null, null, now()::timestamptz, null, true),
    ('Greek Salad', 9.99, 140, 'ebc3892c-c0db-48e0-8329-a4256bb52947', null, null, null, now()::timestamptz, null, true),
    ('Cobb Salad', 10.99, 130, 'ebc3892c-c0db-48e0-8329-a4256bb52947', null, null, null, now()::timestamptz, null, true);
