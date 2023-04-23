package com.konstantion.testcontainers;

import java.util.UUID;

public record MigrationConstants() {
    public static final UUID[] ORDER_IDS = new UUID[]{
            UUID.fromString("0a5119d2-ed97-4b63-8588-14bbc617072e"),
            UUID.fromString("1c08cee9-3ae2-4c6d-9b1a-d8bcbf86accd")
    };
}
