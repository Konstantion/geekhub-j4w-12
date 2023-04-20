INSERT INTO public.product (name, price, weight, category_id, image_bytes, description, creator_id, created_at, deactivate_at, active)
VALUES ('testProduct', 12.2, 23, null, null, 'nice product', null, now()::timestamptz, null, true);

INSERT INTO public.product (name, price, weight, category_id, image_bytes, description, creator_id, created_at, deactivate_at, active)
VALUES ('testProductDisabled', 12.2, 23, null, null, 'nice product', null, now()::timestamptz, null, false);

