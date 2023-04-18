package com.konstantion.user;

import com.konstantion.user.model.CreateUserRequest;
import com.konstantion.user.model.UpdateUserRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.UUID;

public interface UserService extends UserDetailsService {
    default List<User> getAll(boolean onlyActive) {
        return getAll(onlyActive, null);
    }

    default List<User> getAll() {
        return getAll(true);
    }

    List<User> getAll(boolean onlyActive, Role role);

    User getUserById(UUID uuid);

    User createWaiter(CreateUserRequest createUserRequest, User user);

    User createAdmin(CreateUserRequest createUserRequest, User user);

    User addPermission(UUID userId, Permission permission, User user);

    User removePermission(UUID userId, Permission permission, User user);

    User addRole(UUID userId, Role role, User user);

    User removeRole(UUID userId, Role role, User user);

    User update(UUID userId, UpdateUserRequest request, User user);

    User deactivate(UUID userId, User user);

    User activate(UUID userId, User user);

    User delete(UUID userId, User user);
}
