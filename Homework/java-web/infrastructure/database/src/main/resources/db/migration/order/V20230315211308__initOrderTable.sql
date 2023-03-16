create table if not exists public."order"
(
    uuid uuid default gen_random_uuid() not null,
    total_price double precision,
    placed_at timestamp with time zone,
    user_uuid uuid,
    status varchar,
    constraint order_uuid_pkey
        primary key (uuid),
    constraint order_users_fk
        foreign key (user_uuid) references public.user
);