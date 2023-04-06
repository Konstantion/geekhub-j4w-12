ALTER TABLE public.table
    ADD CONSTRAINT fk_table_on_order FOREIGN KEY (order_id) REFERENCES public.order ON DELETE SET NULL;