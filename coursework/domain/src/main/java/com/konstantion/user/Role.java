package com.konstantion.user;

import com.google.common.collect.Sets;

import java.util.Set;

public enum Role {
    ADMIN,
    WAITER,
    TABLE;

    public static Set<Role> getWaiterRole() {
        return  Sets.newHashSet(WAITER);
    }

    public static Set<Role> getAdminRole() {
        return  Sets.newHashSet(ADMIN);
    }
}
