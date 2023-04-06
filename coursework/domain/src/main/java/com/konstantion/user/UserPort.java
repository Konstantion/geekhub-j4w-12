package com.konstantion.user;

import java.util.Optional;
import java.util.UUID;

public interface UserPort {
    User save(User user);
    Optional<User> findById(UUID id);
    void delete(User user);
}