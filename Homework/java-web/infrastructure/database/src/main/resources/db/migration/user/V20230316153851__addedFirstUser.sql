create extension if not exists pgcrypto;
INSERT INTO "user" (uuid, email, first_name, password, last_name, phone_number, enabled, non_locked)
VALUES ('d750e56e-b5e8-11ed-8481-00d8611a4231', 'email', 'first', crypt('password',gen_salt('bf', 8)), 'last', '0500962023', true, true);
