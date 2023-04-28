ALTER TABLE public.order
    ADD CONSTRAINT fk_order_on_bill FOREIGN KEY (bill_id) REFERENCES public.bill ON DELETE SET NULL;