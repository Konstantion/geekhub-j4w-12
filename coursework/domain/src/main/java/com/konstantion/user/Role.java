package com.konstantion.user;

import java.util.Set;

public enum Role {
    ADMIN,
    WAITER,
    TABLE;

    public static Set<Role> getWaiterRoles() {
        return Set.of(WAITER);
    }

    public static Set<Role> getAdminRoles() {
        return Set.of(ADMIN);
    }
}
