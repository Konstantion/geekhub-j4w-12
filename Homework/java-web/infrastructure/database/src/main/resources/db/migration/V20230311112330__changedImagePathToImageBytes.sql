alter table product
    rename column image_path to image_bytes;

alter table product
    alter column image_bytes type bytea using image_bytes::bytea;