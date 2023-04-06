package com.konstantion.adapters.table;

import com.konstantion.table.Table;
import com.konstantion.table.TablePort;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public record TableDatabaseAdapter() implements TablePort {
    @Override
    public Table save(Table table) {
        return null;
    }

    @Override
    public void delete(Table table) {

    }

    @Override
    public Optional<Table> findById(UUID id) {
        return Optional.empty();
    }
}
