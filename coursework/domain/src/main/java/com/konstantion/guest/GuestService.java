package com.konstantion.guest;

import com.konstantion.guest.dto.CreationGuestDto;
import com.konstantion.guest.dto.GuestDto;
import com.konstantion.user.User;

import java.util.UUID;

public interface GuestService {
    GuestDto getById(UUID id);
    GuestDto create(CreationGuestDto cgdto, User user);

    GuestDto edit(UUID id, CreationGuestDto cgdto, User user);

    GuestDto delete(UUID id, User user);
}
