package com.konstantion.table;


import java.util.Optional;
import java.util.UUID;

public interface TablePort {
    Table save(Table table);

    void delete(Table table);

    Optional<Table> findById(UUID id);
}
