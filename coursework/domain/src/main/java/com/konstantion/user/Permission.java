package com.konstantion.user;

import com.google.common.collect.Sets;

import java.util.Set;

public enum Permission {
    //super admin permission
    SUPER_USER,
    //table actions
    CREATE_TABLE, DELETE_TABLE, UPDATE_TABLE, CHANGE_TABLE_STATE,
    ASSIGN_WAITER_TO_TABLE, REMOVE_WAITER_FROM_TABLE,
    //user actions
    CREATE_USER, CHANGE_USER_STATE,
    //product actions
    CREATE_PRODUCT, DELETE_PRODUCT, UPDATE_PRODUCT, CHANGE_PRODUCT_STATE,
    DELETE_PRODUCT_FROM_ORDER, ADD_PRODUCT_TO_ORDER,
    //hall actions
    CREATE_HALL, DELETE_HALL, UPDATE_HALL, CHANGE_HALL_STATE,
    //bill actions
    CREATE_BILL_FROM_ORDER, CANCEL_BILL, GET_BILL, CLOSE_BILL, CHANGE_BILL_STATE,
    //call actions
    CALL_WAITER, CLOSE_CALL,
    //order actions
    OPEN_ORDER, CLOSE_ORDER, TRANSFER_ORDER, DELETE_ORDER,
    //guest actions
    CREATE_GUEST, UPDATE_GUEST, DELETE_GUEST, CHANGE_GUEST_STATE,
    //category actions
    CREATE_CATEGORY, DELETE_CATEGORY, UPDATE_CATEGORY;


    public static Set<Permission> getDefaultWaiterPermission() {
        return Sets.newHashSet(
                OPEN_ORDER, CLOSE_ORDER,
                ADD_PRODUCT_TO_ORDER,
                TRANSFER_ORDER,
                CLOSE_BILL, CREATE_BILL_FROM_ORDER,
                CLOSE_CALL
        );
    }

    public static Set<Permission> getDefaultAdminPermission() {
        Set<Permission> adminPermissions = Sets.newHashSet(
                DELETE_PRODUCT_FROM_ORDER, DELETE_ORDER,
                CREATE_TABLE, UPDATE_TABLE, CHANGE_TABLE_STATE,
                CREATE_USER, CHANGE_USER_STATE,
                CANCEL_BILL, CHANGE_BILL_STATE,
                CREATE_PRODUCT, UPDATE_PRODUCT, CHANGE_PRODUCT_STATE,
                CREATE_HALL, UPDATE_HALL, CHANGE_HALL_STATE,
                CREATE_GUEST, UPDATE_GUEST, CHANGE_GUEST_STATE,
                CREATE_CATEGORY, UPDATE_CATEGORY
        );

        adminPermissions.addAll(getDefaultWaiterPermission());

        return adminPermissions;
    }

    public static Set<Permission> getDefaultTablePermission() {
        return Set.of(
                CALL_WAITER, GET_BILL
        );
    }
}
