package com.konstantion.user;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findUserById(Long id);
}
