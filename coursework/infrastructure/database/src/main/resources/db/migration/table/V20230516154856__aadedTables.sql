INSERT INTO public."table" (name, capacity, table_type, hall_id, order_id, created_at, deleted_at, password, active)
VALUES ('Hall-1 Table-1', 10, 'COMMON', '17b3edee-19f7-4de0-acc1-d3750143c2ac', null, now()::timestamptz, null, crypt('h1t1', gen_salt('bf', 8)), true);

INSERT INTO public."table" (name, capacity, table_type, hall_id, order_id, created_at, deleted_at, password, active)
VALUES ('Hall-1 Table-2', 10, 'COMMON', '17b3edee-19f7-4de0-acc1-d3750143c2ac', null, now()::timestamptz, null, crypt('h1t2', gen_salt('bf', 8)), true);

INSERT INTO public."table" (name, capacity, table_type, hall_id, order_id, created_at, deleted_at, password, active)
VALUES ('Hall-1 Table-3', 10, 'VIP', '17b3edee-19f7-4de0-acc1-d3750143c2ac', null, now()::timestamptz, null, crypt('h1t3', gen_salt('bf', 8)), true);

INSERT INTO public."table" (name, capacity, table_type, hall_id, order_id, created_at, deleted_at, password, active)
VALUES ('Hall-2 Table-1', 10, 'COMMON', 'caa6ca34-cc20-4b97-8b62-340eb82883a3', null, now()::timestamptz, null, crypt('h2t1', gen_salt('bf', 8)), true);

INSERT INTO public."table" (name, capacity, table_type, hall_id, order_id, created_at, deleted_at, password, active)
VALUES ('Hall-2 Table-2', 10, 'COMMON', 'caa6ca34-cc20-4b97-8b62-340eb82883a3', null, now()::timestamptz, null, crypt('h2t2', gen_salt('bf', 8)), true);

INSERT INTO public."table" (name, capacity, table_type, hall_id, order_id, created_at, deleted_at, password, active)
VALUES ('Hall-2 Table-3', 10, 'VIP', 'caa6ca34-cc20-4b97-8b62-340eb82883a3', null, now()::timestamptz, null, crypt('h2t3', gen_salt('bf', 8)), true);

INSERT INTO public."table" (name, capacity, table_type, hall_id, order_id, created_at, deleted_at, password, active)
VALUES ('Hall-3 Table-1', 10, 'COMMON', 'ca27b202-0630-4e6d-b083-ddedadbeb369', null, now()::timestamptz, null, crypt('h3t1', gen_salt('bf', 8)), true);

INSERT INTO public."table" (name, capacity, table_type, hall_id, order_id, created_at, deleted_at, password, active)
VALUES ('Hall-3 Table-2', 10, 'COMMON', 'ca27b202-0630-4e6d-b083-ddedadbeb369', null, now()::timestamptz, null, crypt('h3t2', gen_salt('bf', 8)), true);

INSERT INTO public."table" (name, capacity, table_type, hall_id, order_id, created_at, deleted_at, password, active)
VALUES ('Hall-3 Table-3', 10, 'VIP', 'ca27b202-0630-4e6d-b083-ddedadbeb369', null, now()::timestamptz, null, crypt('h3t3', gen_salt('bf', 8)), true);