package com.konstantion.call;

import com.konstantion.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public record CallServiceImpl(
        CallPort callRepository
) implements CallService {
    private static final Logger logger = LoggerFactory.getLogger(CallServiceImpl.class);
    @Override
    public Call createCall(Purpose purpose, UUID tableId) {
        return null;
    }

    @Override
    public Call closeCall(User user) {
        return null;
    }
}
