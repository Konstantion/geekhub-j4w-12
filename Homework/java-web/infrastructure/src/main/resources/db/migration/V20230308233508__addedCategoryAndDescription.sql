alter table product
    add if not exists description varchar;

alter table product
    add if not exists category varchar;