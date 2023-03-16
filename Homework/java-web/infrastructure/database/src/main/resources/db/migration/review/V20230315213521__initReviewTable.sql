create table if not exists public.review
(
    uuid uuid default gen_random_uuid() not null,
    message varchar(255),
    rating double precision not null,
    user_uuid uuid not null,
    product_uuid uuid not null,
    created_at timestamp with time zone not null,
    primary key (uuid),
    constraint review_users_fk
        foreign key (user_uuid) references public.user
            on delete cascade,
    constraint review_product_fk
        foreign key (product_uuid) references public.product
);

