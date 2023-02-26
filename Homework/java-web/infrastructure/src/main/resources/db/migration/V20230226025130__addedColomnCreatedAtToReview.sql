ALTER TABLE review
    ADD COLUMN IF NOT EXISTS created_at timestamp with time zone;