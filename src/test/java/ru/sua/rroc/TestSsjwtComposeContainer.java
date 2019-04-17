package ru.sua.rroc;

import lombok.extern.slf4j.Slf4j;
import org.testcontainers.containers.DockerComposeContainer;

import java.io.File;

/**
 * pseudosingleton holder for SSJWT testcontainer(docker-compose container) for restcontroller test class
 * <p>
 * database inside composition will be used only for authserver(ssjwt)
 */
@Slf4j
public class TestSsjwtComposeContainer extends DockerComposeContainer<TestSsjwtComposeContainer> {
    private static final int SSJWT_EXPOSED_PORT = 9000;
    private static final String SSJWT_SERVICE_NAME = "ssjwt_1";
    private static final File COMPOSE_FILE = new File("src/test/resources/local-compose.yml");
    private static TestSsjwtComposeContainer container;
    private String url = "";

    private TestSsjwtComposeContainer() {
        super(COMPOSE_FILE);
    }

    public static TestSsjwtComposeContainer getInstance() {
        if (container == null) container = new TestSsjwtComposeContainer()
                .withExposedService(SSJWT_SERVICE_NAME, SSJWT_EXPOSED_PORT)
                .withLocalCompose(true);
        return container;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public void start() {
        super.start();
        url = container.getServiceHost(SSJWT_SERVICE_NAME, SSJWT_EXPOSED_PORT) + ":" +
                container.getServicePort(SSJWT_SERVICE_NAME, SSJWT_EXPOSED_PORT);
        log.info("SSJWT CONTAINER IP ADDRESS IS: {}", url);
    }

    @Override
    public void stop() {
    }
}