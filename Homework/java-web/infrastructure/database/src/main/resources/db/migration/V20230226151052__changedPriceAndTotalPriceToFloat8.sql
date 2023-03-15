alter table product
    alter column price type float8 using price::float8;
alter table public."order"
    alter column total_price type float8 using total_price::float8;