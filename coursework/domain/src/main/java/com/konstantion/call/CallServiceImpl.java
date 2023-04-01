package com.konstantion.call;

import com.konstantion.call.dto.CallDto;
import com.konstantion.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public record CallServiceImpl(
        CallRepository callRepository
) implements CallService {
    private static final Logger logger = LoggerFactory.getLogger(CallServiceImpl.class);
    @Override
    public CallDto createCall(Purpose purpose, UUID tableId) {
        return null;
    }

    @Override
    public CallDto closeCall(User user) {
        return null;
    }
}
