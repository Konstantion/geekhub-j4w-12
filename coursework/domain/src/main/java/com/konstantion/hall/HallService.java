package com.konstantion.hall;

import com.konstantion.hall.model.CreateHallRequest;
import com.konstantion.hall.model.UpdateHallRequest;
import com.konstantion.table.Table;
import com.konstantion.user.User;

import java.util.List;
import java.util.UUID;

public interface HallService {
    Hall create(CreateHallRequest createHallRequest, User user);

    Hall getById(UUID id);

    List<Hall> getAll(boolean onlyActive);

    default List<Hall> getAll() {
        return getAll(true);
    }

    Hall update(UUID id, UpdateHallRequest request, User user);

    Hall activate(UUID id, User user);

    Hall deactivate(UUID id, User user);

    Hall delete(UUID id, User user);

    List<Table> getTablesByHallId(UUID id);
}
