package com.konstantion.user;

import java.util.Set;

public enum Permission {
    CREATE_TABLE, DELETE_TABLE,
    CREATE_PRODUCT, DELETE_PRODUCT, EDIT_PRODUCT,
    CREATE_WAITER, DELETE_WAITER,
    ASSIGN_WAITER_TO_TABLE,
    DELETE_PRODUCT_FROM_ORDER, ADD_PRODUCT_TO_ORDER,
    CREATE_BILL_FROM_TABLE,
    CLOSE_BILL, ACTIVATE_BILL, GET_BILL,
    CLOSE_CALL, CALL_WAITER,
    TRANSFER_ORDER;

    public static Set<Permission> getDefaultTablePermissions() {
        return Set.of(CALL_WAITER, GET_BILL);
    }
}
