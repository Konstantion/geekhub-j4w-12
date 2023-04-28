package com.konstantion.guest;

import com.konstantion.guest.model.CreateGuestRequest;
import com.konstantion.guest.model.UpdateGuestRequest;
import com.konstantion.user.User;

import java.util.List;
import java.util.UUID;

public interface GuestService {
    List<Guest> getAll(boolean onlyActive);

    default List<Guest> getAll() {
        return getAll(true);
    }

    Guest getById(UUID id);

    Guest create(CreateGuestRequest createGuestRequest, User user);

    Guest update(UUID id, UpdateGuestRequest editGuestRequest, User user);

    Guest activate(UUID id, User user);

    Guest deactivate(UUID id, User user);

    Guest delete(UUID id, User user);
}
