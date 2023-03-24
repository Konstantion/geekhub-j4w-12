package com.konstantion.ragistration.token;

import java.time.LocalDateTime;
import java.util.UUID;

public record ConfirmationToken(
        UUID id,
        String token,
        LocalDateTime createdAt,
        LocalDateTime expiresAt,
        LocalDateTime confirmedAt,
        UUID userId
) {
    public ConfirmationToken setConfirmedAt(LocalDateTime confirmedAt) {
        return new ConfirmationToken(id, token, createdAt, expiresAt, confirmedAt, userId);
    }

    public ConfirmationToken setId(UUID id) {
        return new ConfirmationToken(id, token, createdAt, expiresAt, confirmedAt, userId);
    }

    public static ConfirmationTokenBuilder builder() {
        return new ConfirmationTokenBuilder();
    }

    public static final class ConfirmationTokenBuilder {
        private UUID id;
        private String token;
        private LocalDateTime createdAt;
        private LocalDateTime expiresAt;
        private LocalDateTime confirmedAt;
        private UUID userId;

        private ConfirmationTokenBuilder() {
        }

        public ConfirmationTokenBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public ConfirmationTokenBuilder token(String token) {
            this.token = token;
            return this;
        }

        public ConfirmationTokenBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public ConfirmationTokenBuilder expiresAt(LocalDateTime expiredAt) {
            this.expiresAt = expiredAt;
            return this;
        }

        public ConfirmationTokenBuilder confirmedAt(LocalDateTime confirmedAt) {
            this.confirmedAt = confirmedAt;
            return this;
        }

        public ConfirmationTokenBuilder userId(UUID userId) {
            this.userId = userId;
            return this;
        }

        public ConfirmationToken build() {
            return new ConfirmationToken(id, token, createdAt, expiresAt, confirmedAt, userId);
        }
    }
}
