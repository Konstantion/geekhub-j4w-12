package com.konstantion.adapters.user;

import com.konstantion.user.User;
import com.konstantion.user.UserPort;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public record UserDatabaseAdapter() implements UserPort {
    @Override
    public User save(User user) {
        return null;
    }

    @Override
    public Optional<User> findById(UUID id) {
        return Optional.empty();
    }

    @Override
    public void delete(User user) {

    }
}
