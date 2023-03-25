package com.konstantion.category;

import java.time.LocalDateTime;
import java.util.UUID;

public record Category(
        UUID uuid,
        String name,
        LocalDateTime createdAt,
        UUID userUuid
) {

    public static CategoryBuilder builder() {
        return new CategoryBuilder();
    }

    public Category setUuid(UUID uuid) {
        return new Category(uuid, name, createdAt, userUuid);
    }

    public Category setName(String name) {
        return new Category(uuid, name, createdAt, userUuid);
    }

    public static final class CategoryBuilder {
        private UUID uuid;
        private String name;
        private LocalDateTime createdAt;
        private UUID userUuid;

        private CategoryBuilder() {
        }

        public CategoryBuilder uuid(UUID uuid) {
            this.uuid = uuid;
            return this;
        }

        public CategoryBuilder name(String name) {
            this.name = name;
            return this;
        }

        public CategoryBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public CategoryBuilder userUuid(UUID userUuid) {
            this.userUuid = userUuid;
            return this;
        }

        public Category build() {
            return new Category(uuid, name, createdAt, userUuid);
        }
    }
}
