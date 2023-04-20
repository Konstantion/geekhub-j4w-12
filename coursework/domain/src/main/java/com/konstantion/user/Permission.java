package com.konstantion.user;

import java.util.Set;

public enum Permission {
    SUPER_USER,
    CREATE_TABLE, DELETE_TABLE,
    CREATE_PRODUCT, DELETE_PRODUCT, EDIT_PRODUCT,
    CREATE_USER, DELETE_USER, ACTIVATE_USER,
    ASSIGN_WAITER_TO_TABLE, REMOVE_WAITER_FROM_TABLE,
    DELETE_PRODUCT_FROM_ORDER, ADD_PRODUCT_TO_ORDER, SET_PRODUCT_QUANTITY,
    CREATE_BILL_FROM_ORDER,
    CLOSE_BILL, ACTIVATE_BILL, CANCEL_BILL, GET_BILL,
    CALL_WAITER, CLOSE_CALL,
    OPEN_ORDER, CLOSE_ORDER, GET_ORDER, TRANSFER_ORDER, DELETE_ORDER,
    CREATE_GUEST, DELETE_GUEST, DEACTIVATE_USER, ACTIVATE_HALL, DELETE_HALL, ACTIVATE_PRODUCT;


    public static Set<Permission> getDefaultWaiterPermission() {
        return Set.of(
                ADD_PRODUCT_TO_ORDER,
                CREATE_BILL_FROM_ORDER,
                GET_BILL, CLOSE_BILL, TRANSFER_ORDER,
                OPEN_ORDER, CLOSE_ORDER, GET_ORDER
        );
    }

    public static Set<Permission> getDefaultAdminPermission() {
        return Set.of(
                ADD_PRODUCT_TO_ORDER,
                CREATE_BILL_FROM_ORDER,
                GET_BILL, CLOSE_BILL, TRANSFER_ORDER,
                OPEN_ORDER, CLOSE_ORDER, GET_ORDER
        );
    }

    public static Set<Permission> getDefaultTablePermission() {
        return Set.of(
                CALL_WAITER, GET_BILL
        );
    }
}
