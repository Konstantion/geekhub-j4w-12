package com.konstantion.table;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TablePort {
    Table save(Table table);

    void delete(Table table);

    Optional<Table> findById(UUID id);

    List<Table> findAll();

    List<Table> findAllWhereHallId(UUID hallId);

    Optional<Table> findByName(String name);
    void deleteAll();
}
