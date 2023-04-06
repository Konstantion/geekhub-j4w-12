package com.konstantion.call;

import com.konstantion.user.User;

import java.util.UUID;

public interface CallService {
    Call createCall(Purpose purpose, UUID tableId);

    Call closeCall(User user);
}
