package com.konstantion.user;

import com.konstantion.user.model.CreateUserRequest;

import java.util.UUID;

public interface UserService {
    User getUserById(UUID uuid);

    User createWaiter(CreateUserRequest createUserRequest, User user);
}
