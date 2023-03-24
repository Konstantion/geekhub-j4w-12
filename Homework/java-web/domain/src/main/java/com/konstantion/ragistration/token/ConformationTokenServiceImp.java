package com.konstantion.ragistration.token;

import com.konstantion.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.time.LocalDateTime.now;

@Component
public record ConformationTokenServiceImp(
        ConfirmationTokenRepository tokenRepository
) implements ConfirmationTokenService {

    private static final long CONFIRMATION_TIME_MINUTES = 15L;
    private static Logger logger = LoggerFactory.getLogger(ConformationTokenServiceImp.class);

    @Override
    public void saveConfirmationToken(ConfirmationToken token) {
        tokenRepository.save(token);
    }

    @Override
    public Optional<ConfirmationToken> getToken(String token) {
        return tokenRepository.findByToken(token);
    }

    @Override
    public int setConfirmedAt(String token) {
        LocalDateTime confirmedAt = now();
        return tokenRepository.updateConfirmedAt(token, confirmedAt);
    }

    @Override
    public Optional<List<ConfirmationToken>> getTokens(User existingUser) {
        return tokenRepository.findByUserId(existingUser.getId());
    }

    @Override
    public void createAndSaveConfirmationToken(String token, User user) {
        ConfirmationToken confirmationToken = ConfirmationToken.builder()
                .token(token)
                .createdAt(now())
                .expiresAt(now().plusMinutes(CONFIRMATION_TIME_MINUTES))
                .userId(user.getId())
                .build();

        saveConfirmationToken(confirmationToken);
    }
}
