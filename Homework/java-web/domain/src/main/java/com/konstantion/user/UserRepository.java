package com.konstantion.user;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    Optional<User> findUserByUuid(UUID uuid);
}
