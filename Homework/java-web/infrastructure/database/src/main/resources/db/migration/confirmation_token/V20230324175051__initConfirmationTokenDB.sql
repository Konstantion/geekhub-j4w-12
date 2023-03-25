create table confirmation_token
(
    uuid          uuid default gen_random_uuid()
        constraint confirmation_token_pk
            primary key,
    token         varchar     not null
        constraint confirmation_token_pk2
            unique,
    created_at    timestamptz not null,
    expires_at   timestamptz not null,
    confirmed_at timestamptz,
    user_uuid     uuid        not null
        constraint confirmation_token_user_uuid_fk
            references "user" on delete cascade
);