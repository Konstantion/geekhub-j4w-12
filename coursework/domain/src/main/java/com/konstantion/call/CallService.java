package com.konstantion.call;

import com.konstantion.call.model.CreateCallRequest;
import com.konstantion.user.User;

import java.util.List;
import java.util.UUID;

public interface CallService {
    List<Call> getAll();

    List<Call> getAllByUser(User user);

    Call createCall(CreateCallRequest createCallRequest);

    Call closeCall(UUID callId, User user);
}
