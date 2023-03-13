package com.konstantion.user;

import java.util.Set;

public enum Role {
    ADMIN,
    WAITER,
    TABLE;

    public static Set<Role> getTableRole() {
        return Set.of(TABLE);
    }
}
