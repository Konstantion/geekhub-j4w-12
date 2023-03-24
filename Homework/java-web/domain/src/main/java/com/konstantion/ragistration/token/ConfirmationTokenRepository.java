package com.konstantion.ragistration.token;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConfirmationTokenRepository {
    ConfirmationToken save(ConfirmationToken confirmationToken);

    Optional<ConfirmationToken> findByToken(String token);

    Optional<List<ConfirmationToken>> findByUserId(UUID id);

    int updateConfirmedAt(
            String token,
            LocalDateTime confirmedAt
    );
}
