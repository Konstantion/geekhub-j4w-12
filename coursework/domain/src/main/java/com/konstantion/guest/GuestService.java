package com.konstantion.guest;

import com.konstantion.guest.model.CreateGuestRequest;
import com.konstantion.guest.model.EditGuestRequest;
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

    Guest edit(UUID id, EditGuestRequest editGuestRequest, User user);

    Guest delete(UUID id, User user);
}
