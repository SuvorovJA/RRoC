package ru.sua.rroc;

import lombok.extern.slf4j.Slf4j;
import org.testcontainers.containers.PostgreSQLContainer;


/**
 * pseudosingleton holder for Postgresql 10 testcontainer(docker container) for all tests classes
 */
@Slf4j
public class TestPostgresqlContainer extends PostgreSQLContainer<TestPostgresqlContainer> {
    private static final String IMAGE_VERSION = "postgres:10";
    private static TestPostgresqlContainer container;

    private TestPostgresqlContainer() {
        super(IMAGE_VERSION);
    }

    public static TestPostgresqlContainer getInstance() {
        if (container == null) container = new TestPostgresqlContainer()
                .withDatabaseName("integration-test")
                .withUsername("postgres")
                .withPassword("postgres");
        return container;
    }

    @Override
    public void start() {
        super.start();
        // system properties (env vars) override application.yml settings
        System.setProperty("SPRING_DATASOURCE_URL", container.getJdbcUrl());
        System.setProperty("SPRING_DATASOURCE_USERNAME", container.getUsername());
        System.setProperty("SPRING_DATASOURCE_PASSWORD", container.getPassword());
        log.info("POSTGRESQL CONTAINER JDBC URL IS: {}", container.getJdbcUrl());
        log.info("POSTGRESQL CONTAINER USER IS: {}", container.getUsername());
        log.info("POSTGRESQL CONTAINER PASSWORD IS: {}", container.getPassword());
        log.info("POSTGRESQL CONTAINER INFO IS: {}", container.getContainerInfo().toString());
    }

    @Override
    public void stop() {
        // dont use stop() due java.lang.StackOverflowError here
//        container.stop();
    }
}