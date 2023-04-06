package com.konstantion.user;

import com.konstantion.user.model.UpdateUserRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.UUID;

public interface UserService extends UserDetailsService {
    String signUpUser(User user);

    UUID enableUser(User user);

    UUID editUser(UUID uuid, UpdateUserRequest updateDto, User requester);

    UUID deleteUser(UUID uuid, User requester);

    User getUser(UUID uuid, User requester);
}
