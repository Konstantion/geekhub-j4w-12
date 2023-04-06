package com.konstantion.guest;

import java.util.Optional;
import java.util.UUID;

public interface GuestPort {
    Optional<Guest> findById(UUID uuid);

    Guest save(Guest guest);

    void delete(Guest guest);
}
