CREATE TABLE call
(
    id        UUID NOT NULL DEFAULT gen_random_uuid(),
    table_id  UUID NOT NULL,
    purpose   VARCHAR(255),
    opened_at TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_call PRIMARY KEY (id),
    CONSTRAINT fk_call_on_table FOREIGN KEY (table_id) REFERENCES public."table" ON DELETE CASCADE
);

