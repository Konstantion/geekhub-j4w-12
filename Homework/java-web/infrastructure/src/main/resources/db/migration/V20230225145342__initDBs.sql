CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

create table if not exists users
(
    id bigserial,
    uuid uuid not null,
    email varchar(100) not null,
    username varchar(100) not null,
    password varchar(255) not null,
    constraint user_id_pkey
        primary key (id),
    unique (uuid),
    unique (email),
    unique (username)
);

create table if not exists product
(
    id bigserial,
    uuid uuid not null,
    name varchar(255) not null,
    price integer not null,
    created_at timestamp with time zone,
    user_uuid uuid,
    primary key (id),
    constraint product_key
        unique (uuid),
    constraint product_users_fkey
        foreign key (user_uuid) references users (uuid)
);

create table if not exists public.order
(
    id bigserial,
    uuid uuid not null,
    total_price integer,
    placed_at timestamp with time zone,
    user_uuid uuid,
    constraint order_id_pkey
        primary key (id),
    constraint order_uuid_pkey
        unique (uuid),
    constraint order_users_fkey
        foreign key (user_uuid) references users (uuid)
);

create table if not exists review
(
    id bigserial,
    uuid uuid not null,
    message varchar(255),
    rating integer,
    user_uuid uuid,
    product_uuid uuid,
    primary key (id),
    constraint review_key
        unique (uuid),
    constraint review_users_fkey
        foreign key (user_uuid) references users (uuid)
            on delete cascade,
    constraint review_product_fk
        foreign key (product_uuid) references product (uuid)
);

create table if not exists order_product
(
    order_id bigserial,
    product_id bigserial,
    quantity integer not null,
    constraint order_products_order_fkey
        foreign key (order_id) references public.order,
    constraint order_products_product_fkey
        foreign key (product_id) references product
);