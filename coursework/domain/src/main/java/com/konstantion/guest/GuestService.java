package com.konstantion.guest;

import com.konstantion.guest.model.CreateGuestRequest;
import com.konstantion.guest.model.EditGuestRequest;
import com.konstantion.user.User;

import java.util.UUID;

public interface GuestService {
    Guest getById(UUID id);
    Guest create(CreateGuestRequest createGuestRequest, User user);

    Guest edit(UUID id, EditGuestRequest editGuestRequest, User user);

    Guest delete(UUID id, User user);
}
