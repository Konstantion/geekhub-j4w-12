package com.konstantion.user;

import java.util.Set;

public enum Permission {
    CREATE_TABLE, DELETE_TABLE,
    CREATE_PRODUCT, DELETE_PRODUCT, EDIT_PRODUCT,
    CREATE_WAITER, DELETE_WAITER,
    ASSIGN_WAITER_TO_TABLE,
    DELETE_PRODUCT_FROM_ORDER, ADD_PRODUCT_TO_ORDER,
    CREATE_BILL_FROM_ORDER,
    CLOSE_BILL, ACTIVATE_BILL, GET_BILL,
    CLOSE_CALL, CALL_WAITER,
    OPEN_ORDER, CLOSE_ORDER, GET_ORDER,
    ADD_GUEST, DELETE_GUEST,
    TRANSFER_ORDER;

    public static Set<Permission> getDefaultWaiterPermission() {
        return Set.of(
                ADD_PRODUCT_TO_ORDER,
                CREATE_BILL_FROM_ORDER,
                GET_BILL, CLOSE_BILL, TRANSFER_ORDER,
                OPEN_ORDER, CLOSE_ORDER, GET_ORDER
        );
    }
}
