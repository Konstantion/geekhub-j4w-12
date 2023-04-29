package com.konstantion.user;

import java.util.Set;

public enum Permission {
    SUPER_USER,
    //table actions
    CREATE_TABLE, DELETE_TABLE, UPDATE_TABLE, CHANGE_TABLE_STATE,
    ASSIGN_WAITER_TO_TABLE, REMOVE_WAITER_FROM_TABLE,
    //user actions
    CREATE_USER, DELETE_USER, CHANGE_USER_STATE,
    //product actions
    CREATE_PRODUCT, DELETE_PRODUCT, UPDATE_PRODUCT, CHANGE_PRODUCT_STATE,
    DELETE_PRODUCT_FROM_ORDER, ADD_PRODUCT_TO_ORDER, SET_PRODUCT_QUANTITY,
    //hall actions
    CREATE_HALL, DELETE_HALL, UPDATE_HALL, CHANGE_HALL_STATE,
    //bill actions
    CREATE_BILL_FROM_ORDER, CANCEL_BILL, GET_BILL, CLOSE_BILL, CHANGE_BILL_STATE,
    //call actions
    CALL_WAITER, CLOSE_CALL,
    //order actions
    OPEN_ORDER, CLOSE_ORDER, GET_ORDER, TRANSFER_ORDER, DELETE_ORDER,
    //guest actions
    CREATE_GUEST, UPDATE_GUEST, DELETE_GUEST, CHANGE_GUEST_STATE;


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
