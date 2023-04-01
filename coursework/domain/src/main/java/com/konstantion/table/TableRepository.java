package com.konstantion.table;


import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TableRepository  {
    Table save(Table table);

    void delete(Table table);

    Optional<Table> findById(UUID id);
}
