create table if not exists public.product
(
    uuid          uuid default gen_random_uuid() not null,
    name          varchar(255)                   not null,
    price         double precision               not null,
    created_at    timestamp with time zone       not null,
    user_uuid     uuid,
    image_bytes   bytea,
    description   varchar,
    category_uuid uuid,
    primary key (uuid),
    constraint product_users_fk
        foreign key (user_uuid) references public.user on delete set null,
    constraint product_category_fk
        foreign key (category_uuid) references public.category on delete set null
);

