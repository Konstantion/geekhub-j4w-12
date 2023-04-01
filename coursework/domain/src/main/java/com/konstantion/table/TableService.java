package com.konstantion.table;

import com.konstantion.table.dto.CreationTableDto;
import com.konstantion.table.dto.TableDto;
import com.konstantion.user.User;

import java.util.UUID;

public interface TableService {
    TableDto activate(UUID tableId, User user);

    TableDto deactivate(UUID tableId, User user);

    TableDto create(CreationTableDto creationTableDto, User user);

    /**
     * This method isn't safe and delete entity in DB,
     * witch can lead to the destruction of relationships with other entities,
     * if you want to safely disable entity use {@link #deactivate(UUID, User)} instead.
     */
    TableDto delete(UUID tableId, User user);

    TableDto getById(UUID tableId, User user);

    TableDto clearOrder(UUID tableId, User user);

    TableDto setOrder(UUID tableId, UUID orderId, User user);
}
