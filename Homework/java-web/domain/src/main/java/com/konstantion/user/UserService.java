package com.konstantion.user;

import com.konstantion.user.model.CreateUserRequest;
import com.konstantion.user.model.RestoreUserRequest;
import com.konstantion.user.model.UpdateUserRequest;
import com.konstantion.user.model.UpdateUserRolesRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.UUID;

public interface UserService extends UserDetailsService {
    String signUpUser(User user);

    UUID enableUser(UUID uuid);

    UUID disableUser(UUID uuid);

    User editUser(UUID uuid, UpdateUserRequest updateUserRequest, User authorized);

    User editUserRoles(UUID uuid, UpdateUserRolesRequest updateUserRolesRequest);

    String restorePassword(String email);

    User getUser(UUID uuid, User requester);

    User createAdmin(CreateUserRequest request);

    User createModerator(CreateUserRequest toCreateUserRequest);

    User delete(UUID id);
}
