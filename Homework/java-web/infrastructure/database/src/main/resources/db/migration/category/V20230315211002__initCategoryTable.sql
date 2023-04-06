create table if not exists public.category
(
    uuid uuid default gen_random_uuid() not null,
    name varchar not null,
    created_at timestamp with time zone not null,
    user_uuid uuid,
    constraint category_pk
        primary key (uuid),
    constraint category_user_fk
        foreign key (user_uuid) references public.user on delete set null
);

