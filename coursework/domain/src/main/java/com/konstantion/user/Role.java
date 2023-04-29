package com.konstantion.user;

import java.util.Set;

public enum Role {
    ADMIN,
    WAITER,
    TABLE;

    public static Set<Role> getWaiterRole() {
        return Set.of(WAITER);
    }

    public static Set<Role> getAdminRole() {
        return Set.of(ADMIN);
    }
}
