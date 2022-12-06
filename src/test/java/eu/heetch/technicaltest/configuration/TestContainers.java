package eu.heetch.technicaltest.configuration;

import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

public class TestContainers {

    public static JdbcDatabaseContainer<?> postgresContainer() {
        DockerImageName dockerImageName = DockerImageName.parse("registry.hub.docker.com/postgis/postgis")
                .asCompatibleSubstituteFor("postgres");

        return new PostgreSQLContainer<>(dockerImageName);
    }
}
