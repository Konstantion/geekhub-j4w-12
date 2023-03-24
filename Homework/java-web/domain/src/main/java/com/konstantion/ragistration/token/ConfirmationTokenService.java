package com.konstantion.ragistration.token;

import com.konstantion.user.User;

import java.util.List;
import java.util.Optional;

public interface ConfirmationTokenService {
    void saveConfirmationToken(ConfirmationToken token);

    Optional<ConfirmationToken> getToken(String token);

    int setConfirmedAt(String token);

    Optional<List<ConfirmationToken>> getTokens(User existingUser);

    void createAndSaveConfirmationToken(String token, User user);
}
