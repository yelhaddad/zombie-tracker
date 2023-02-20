package eu.yelhaddad.zombietracker.configuration;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.JdbcDatabaseContainer;

public class PostgresContainerExtension implements BeforeAllCallback {

    private static final JdbcDatabaseContainer<?> postgres = TestContainers.postgresContainer();

    @Override
    public void beforeAll(ExtensionContext context) {
        if (postgres.isRunning())
            return;

        postgres.start();

        System.setProperty("spring.datasource.url", postgres.getJdbcUrl());
        System.setProperty("spring.datasource.hikari.username", postgres.getUsername());
        System.setProperty("spring.datasource.hikari.password", postgres.getPassword());
    }
}
