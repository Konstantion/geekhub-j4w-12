create table if not exists public.user
(
    uuid uuid default gen_random_uuid() not null,
    email varchar(100) not null,
    first_name varchar(100) not null,
    password varchar(255) not null,
    last_name varchar(100) not null,
    phone_number varchar,
    enabled boolean not null default false,
    non_locked boolean not null default true,
    constraint user_uuid_pkey
        primary key (uuid),
    unique (email)
);

