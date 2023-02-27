INSERT INTO product (uuid, name, price, created_at)
VALUES ('66ba3741-0de9-43dc-8fe5-df186c441d61', 'Chocolate', 20, now()::timestamptz);
INSERT INTO product (uuid, name, price, created_at)
VALUES ('d1f9a3d5-de29-460c-aa49-31733feec0c9', 'Rothmans Blue', 87, now()::timestamptz);
INSERT INTO product (uuid, name, price, created_at)
VALUES ('a5310299-54c6-4e23-9bfb-ee7d44853b11', 'Winston Silver', 83, now()::timestamptz);
INSERT INTO product (uuid, name, price, created_at)
VALUES ('1b80da87-35aa-4edf-807c-53a572490e28', 'Kent Nano Silver', 1, now()::timestamptz);
INSERT INTO product (uuid, name, price, created_at)
VALUES ('ef523f1b-f75a-44f6-aac5-a67de5043296', 'Camel Yellow', 1, now()::timestamptz);
INSERT INTO product (uuid, name, price, created_at)
VALUES ('74cdbb2f-b804-40c5-a1d6-4d95b6916a5f', 'Marlboro Gold', 89, now()::timestamptz);

INSERT INTO review (uuid, message, rating, user_uuid, product_uuid, created_at)
VALUES ('1754a99a-3ba0-4cf8-9020-be789feca006', 'Cool message', 3, 'd750e56e-b5e8-11ed-8481-00d8611a4231',  'd1f9a3d5-de29-460c-aa49-31733feec0c9', now()::timestamptz);

INSERT INTO review (uuid, message, rating, user_uuid, product_uuid, created_at)
VALUES ('ae6de1a4-aa65-4d10-bcac-4733255d9e12', 'Very cool message', 4, 'd750e56e-b5e8-11ed-8481-00d8611a4231',  'd1f9a3d5-de29-460c-aa49-31733feec0c9', now()::timestamptz);

INSERT INTO review (uuid, message, rating, user_uuid, product_uuid, created_at)
VALUES ('9dbe7332-e8ca-46ed-bc73-c29df22542e9', 'Very yellow message', 4, 'd750e56e-b5e8-11ed-8481-00d8611a4231',  'ef523f1b-f75a-44f6-aac5-a67de5043296', now()::timestamptz);

INSERT INTO review (uuid, message, rating, user_uuid, product_uuid, created_at)
VALUES ('ff14e585-2708-4af3-8b07-4c226265c057', 'Very Gold message', 5, 'd750e56e-b5e8-11ed-8481-00d8611a4231',  '74cdbb2f-b804-40c5-a1d6-4d95b6916a5f', now()::timestamptz);