package com.konstantion.call;

import com.konstantion.call.dto.CallDto;
import com.konstantion.user.User;

import java.util.UUID;

public interface CallService {
    CallDto createCall(Purpose purpose, UUID tableId);

    CallDto closeCall(User user);
}
