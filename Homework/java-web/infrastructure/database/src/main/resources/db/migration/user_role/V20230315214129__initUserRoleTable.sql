create table if not exists public.user_role
(
    user_uuid uuid not null,
    name varchar not null,
    constraint user_role_users_uuid_fk
        foreign key (user_uuid) references public.user
            on delete cascade
);

