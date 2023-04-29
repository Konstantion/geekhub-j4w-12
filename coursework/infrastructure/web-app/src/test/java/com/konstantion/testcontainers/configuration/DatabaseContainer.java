package com.konstantion.testcontainers.configuration;

import org.springframework.boot.test.context.TestConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;

@TestConfiguration
public class DatabaseContainer extends PostgreSQLContainer<DatabaseContainer> {
    private static final String IMAGE_VERSION = "postgres:14.6";
    private static DatabaseContainer container;

    public DatabaseContainer(String dockerImageName) {
        super(dockerImageName);
    }

    private DatabaseContainer() {
        super(IMAGE_VERSION);
    }

    public static DatabaseContainer getInstance() {
        if (container == null) {
            container = new DatabaseContainer();
        }

        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("DB_URL", container.getJdbcUrl());
        System.setProperty("DB_USERNAME", container.getUsername());
        System.setProperty("DB_PASSWORD", container.getPassword());

    }

    @Override
    public void stop() {
    }
}