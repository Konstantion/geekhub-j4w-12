package com.konstantion.user;

import com.konstantion.user.model.CreateUserRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.UUID;

public interface UserService extends UserDetailsService {
    User getUserById(UUID uuid);

    User createWaiter(CreateUserRequest createUserRequest, User user);
}
