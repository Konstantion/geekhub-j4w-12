package com.konstantion.category;

import java.util.UUID;

public record Category(UUID uuid, String name) {

    public static CategoryBuilder builder() {
        return new CategoryBuilder();
    }
    public static final class CategoryBuilder {
        private UUID uuid;
        private String name;

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

        public Category build() {
            return new Category(uuid, name);
        }
    }
}
