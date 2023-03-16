create table if not exists public.order_product
(
    order_uuid uuid not null,
    product_uuid uuid not null,
    quantity integer not null,
    constraint order_products_order_fk
        foreign key (order_uuid) references public."order",
    constraint order_products_product_fk
        foreign key (product_uuid) references public.product
);

