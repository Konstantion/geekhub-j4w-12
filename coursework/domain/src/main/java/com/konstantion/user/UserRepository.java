package com.konstantion.user;

import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository {
    User save(User user);
    Optional<User> findById(UUID id);
    void delete(User user);
}