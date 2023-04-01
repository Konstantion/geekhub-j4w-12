package com.konstantion.user;

import java.util.Set;

public enum Role {
    ADMIN,
    WAITER;

    public static Set<Role> getWaiterRole() {
        return Set.of(WAITER);
    }
}
