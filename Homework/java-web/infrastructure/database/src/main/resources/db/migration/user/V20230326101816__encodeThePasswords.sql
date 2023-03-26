create extension if not exists pgcrypto;
UPDATE public."user"
    SET password = crypt(password,gen_salt('bf', 8));