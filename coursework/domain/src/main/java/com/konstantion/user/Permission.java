package com.konstantion.user;

import java.util.Set;

public enum Permission {
    SUPER_USER,
    CREATE_TABLE, DELETE_TABLE, UPDATE_TABLE,
    ASSIGN_WAITER_TO_TABLE, REMOVE_WAITER_FROM_TABLE,
    CREATE_PRODUCT, DELETE_PRODUCT, UPDATE_PRODUCT,
    CREATE_USER, DELETE_USER, CHANGE_USER_STATE,
    DELETE_PRODUCT_FROM_ORDER, ADD_PRODUCT_TO_ORDER, SET_PRODUCT_QUANTITY,
    CREATE_BILL_FROM_ORDER,
    CLOSE_BILL, CHANGE_BILL_STATE, CANCEL_BILL, GET_BILL,
    CALL_WAITER, CLOSE_CALL,
    OPEN_ORDER, CLOSE_ORDER, GET_ORDER, TRANSFER_ORDER, DELETE_ORDER,
    CREATE_GUEST, DELETE_GUEST, CHANGE_HALL_STATE, DELETE_HALL, CHANGE_PRODUCT_STATE, CHANGE_TABLE_STATE, CHANGE_GUEST_STATE;


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
