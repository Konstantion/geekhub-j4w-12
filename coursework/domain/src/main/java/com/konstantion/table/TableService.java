package com.konstantion.table;

import com.konstantion.order.Order;
import com.konstantion.table.model.CreateTableRequest;
import com.konstantion.user.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TableService extends UserDetailsService {
    List<Table> getAll();

    Table activate(UUID tableId, User user);

    Table deactivate(UUID tableId, User user);

    Table create(CreateTableRequest creationTable, User user);

    /**
     * This method isn't safe and delete entity in DB,
     * witch can lead to the destruction of relationships with other entities,
     * if you want to safely disable entity use {@link #deactivate(UUID, User)} instead.
     */
    Table delete(UUID tableId, User user);

    Table getById(UUID tableId);

    Order getOrderByTableId(UUID tableId);
}
