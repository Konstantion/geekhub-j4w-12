package com.konstantion.api.configuration;

import com.konstantion.user.Permission;
import com.konstantion.user.Role;
import com.konstantion.user.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;

public record UserDetailsFactory() {
    public static UserDetails waiter() {
        return User.builder()
                .id(UUID.randomUUID())
                .permissions(Permission.getDefaultWaiterPermission())
                .roles(Role.getWaiterRoles())
                .email("waiter")
                .password("waiter")
                .active(true)
                .build();
    }
}
