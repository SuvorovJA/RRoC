package ru.sua.rroc;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;

/**
 * workaround for flyway use testcontainers datasource instead application datasource
 * use '@ContextConfiguration(initializers = IntegrationTestInitializer.class)'
 * on test classes
 */
public class IntegrationTestInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static PostgreSQLContainer postgreSQLContainer = TestPostgresqlContainer.getInstance();

    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
        TestPropertyValues values = TestPropertyValues.of(
                "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                "spring.datasource.password=" + postgreSQLContainer.getPassword(),
                "spring.datasource.username=" + postgreSQLContainer.getUsername()
        );
        values.applyTo(configurableApplicationContext);
    }
}