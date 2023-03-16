package com.konstantion.user;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    Optional<User> findUserById(UUID uuid);

    User save(User user);

    UUID setEnableById(UUID id, boolean enable);

    UUID setNonLockedById(UUID id, boolean active);

    Optional<User> findUserByEmail(String email);
}
