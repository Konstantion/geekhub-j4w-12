alter table review
    alter column rating type float8 using rating::float8;