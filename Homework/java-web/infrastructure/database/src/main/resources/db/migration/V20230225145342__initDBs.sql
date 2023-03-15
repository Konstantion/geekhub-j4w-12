CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

create table if not exists users
(
    uuid     uuid default gen_random_uuid(),
    email    varchar(100) not null,
    username varchar(100) not null,
    password varchar(255) not null,
    constraint user_uuid_pkey
        primary key (uuid),
    unique (uuid),
    unique (email),
    unique (username)
);

create table if not exists product
(
    uuid       uuid default gen_random_uuid(),
    name       varchar(255) not null,
    price      integer      not null,
    created_at timestamp with time zone,
    user_uuid  uuid,
    primary key (uuid),
    constraint product_users_fkey
        foreign key (user_uuid) references users (uuid)
);

create table if not exists public.order
(
    uuid        uuid default gen_random_uuid(),
    total_price integer,
    placed_at   timestamp with time zone,
    user_uuid   uuid,
    constraint order_uuid_pkey
        primary key (uuid),
    constraint order_users_fkey
        foreign key (user_uuid) references users (uuid)
);

create table if not exists review
(
    uuid         uuid default gen_random_uuid(),
    message      varchar(255),
    rating       integer,
    user_uuid    uuid,
    product_uuid uuid,
    primary key (uuid),
    constraint review_users_fkey
        foreign key (user_uuid) references users (uuid)
            on delete cascade,
    constraint review_product_fk
        foreign key (product_uuid) references product (uuid)
);

create table if not exists order_product
(
    order_uuid   uuid    not null,
    product_uuid uuid    not null,
    quantity     integer not null,
    constraint order_products_order_fkey
        foreign key (order_uuid) references public.order,
    constraint order_products_product_fkey
        foreign key (product_uuid) references product
);