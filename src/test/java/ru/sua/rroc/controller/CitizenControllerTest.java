package ru.sua.rroc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import ru.sua.rroc.domain.Citizen;
import ru.sua.rroc.domain.CitizenDTO;

import java.net.URI;

import static org.junit.Assert.*;

@Slf4j
@RunWith(SpringRunner.class)
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CitizenControllerTest {

    private static final String ethalonId5Json = "{\"id\":5,\"fullName\":\"Mickey-Treutel\",\"dob\":\"1996-03-19\",\"address\":\"90 Bunker Hill Terrace\",\"dulnumber\":\"205343909704\"}";
    private static final String ethalonId5ModifiedJson = "{\"id\":5,\"fullName\":\"Mickey-Treutel\",\"dob\":\"1996-03-19\",\"address\":\"New address\",\"dulnumber\":\"205343909704\"}";
    private static final String ethalonNewJson = "{\"fullName\":\"Mickey-Mouse\",\"dob\":\"1996-06-06\",\"address\":\"Disney\",\"dulnumber\":\"0000000001\"}";
    private static final String ethalonNewIncorrectJson = "{\"fullName\":\"Donald Duck\",\"dulnumber\":\"2\"}";
    private static final String oauthClientCredentials = "Basic Y2xpZW50SWQ6c2VjcmV0";    // clientId:secret
    private CitizenDTO ethalonId5;
    private Citizen ethalonId5ModifiedAsCitizenDomainClass;
    private Citizen ethalonNewAsCitizenDomainClass;
    private CitizenDTO ethalonNewIncorrect;
    private String jwtToken;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private WebTestClient testClient;

    @Autowired
    private ObjectMapper mapper;

    @Before
    public void setUp() throws Exception {
        ethalonId5 = mapper.readValue(ethalonId5Json, CitizenDTO.class);
        ethalonId5ModifiedAsCitizenDomainClass = mapper.readValue(ethalonId5ModifiedJson, Citizen.class);
        ethalonNewAsCitizenDomainClass = mapper.readValue(ethalonNewJson, Citizen.class);
        ethalonNewIncorrect = mapper.readValue(ethalonNewIncorrectJson, CitizenDTO.class);

        getAndInstallAuthTokenFromServer("faro", "faro-password");

    }

    private void getAndInstallAuthTokenFromServer(String login, String password) {
        WebTestClient.ResponseSpec response = testClient
                .post()
                .uri("http://localhost:9000/oauth/token?grant_type=password&username=" + login + "&password=" + password)
                .headers(h -> h.add("Authorization", oauthClientCredentials))
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(BodyInserters.fromObject(OAuth2AccessToken.class))
                .exchange().expectStatus().isOk();
        OAuth2AccessToken authToken = response.expectBody(OAuth2AccessToken.class).returnResult().getResponseBody();
        assertNotNull(authToken);
        log.info("TOKEN FROM AUTHSERVER IS \'{}\'", authToken.getValue());
        jwtToken = "bearer " + authToken.getValue();
    }

    @Test
    public void getCitizenById() {
        WebTestClient.ResponseSpec response = testClient.get().uri("/citizens/5")
                .headers(h -> h.add("Authorization", jwtToken))
                .exchange().expectStatus().isOk();
        CitizenDTO citizen = response.expectBody(CitizenDTO.class).returnResult().getResponseBody();
        assertEquals(ethalonId5, citizen);
    }

    @Test
    public void attemptGetCitizenByIdWithoutAuthentication() {
        testClient.get().uri("/citizens/5").exchange().expectStatus().isUnauthorized();
    }

    @Test
    public void zza__createCitizen() {
        WebTestClient.ResponseSpec response = testClient.post().uri("/citizens")
                .headers(h -> h.add("Authorization", jwtToken))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(BodyInserters.fromObject(ethalonNewAsCitizenDomainClass))
                .exchange().expectStatus().isCreated();

        URI location = getLocationUri(response);

        String[] path = location.getPath().split("/");
        String id = path[path.length - 1];
        assertNotNull(id);

        // reread created
        WebTestClient.ResponseSpec responseSecond = testClient.get().uri("/citizens/" + id)
                .headers(h -> h.add("Authorization", jwtToken))
                .exchange().expectStatus().isOk();
        CitizenDTO citizen = responseSecond.expectBody(CitizenDTO.class).returnResult().getResponseBody();
        assertNotNull(citizen);
        assertEquals(ethalonNewAsCitizenDomainClass.getDulnumber(), citizen.getDulnumber());
    }

    @Test
    public void createIncorrectCitizen() {
        testClient.post().uri("/citizens")
                .headers(h -> h.add("Authorization", jwtToken))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(BodyInserters.fromObject(ethalonNewIncorrect))
                .exchange().expectStatus().isBadRequest();
    }

    @Test
    public void attemptDeleteCitizenWithoutAuthorization() {
        getAndInstallAuthTokenFromServer("ro", "ro-password");
        testClient.delete().uri("/citizens/5")
                .headers(h -> h.add("Authorization", jwtToken))
                .exchange().expectStatus().isForbidden();
    }

    @Test
    public void zzz__deleteCitizen() {
        testClient.get().uri("/citizens/5")
                .headers(h -> h.add("Authorization", jwtToken))
                .exchange().expectStatus().isOk();
        testClient.delete().uri("/citizens/5")
                .headers(h -> h.add("Authorization", jwtToken))
                .exchange().expectStatus().isOk();
        testClient.get().uri("/citizens/5")
                .headers(h -> h.add("Authorization", jwtToken))
                .exchange().expectStatus().isNotFound();
    }

    @Test
    public void updateCitizen() {
        // первое чтение
        WebTestClient.ResponseSpec responseFirst = testClient.get().uri("/citizens/5")
                .headers(h -> h.add("Authorization", jwtToken))
                .exchange().expectStatus().isOk();
        CitizenDTO citizenFirst = responseFirst.expectBody(CitizenDTO.class).returnResult().getResponseBody();
        assertEquals(ethalonId5, citizenFirst);

        // модификация
        WebTestClient.ResponseSpec response = testClient.put().uri("/citizens/5")
                .headers(h -> h.add("Authorization", jwtToken))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(BodyInserters.fromObject(ethalonId5ModifiedAsCitizenDomainClass))
                .exchange().expectStatus().isCreated();

        URI location = getLocationUri(response);

        assertEquals("/citizens/5", location.getPath());

        // повторное чтение
        WebTestClient.ResponseSpec responseSecond = testClient.get().uri("/citizens/5")
                .headers(h -> h.add("Authorization", jwtToken))
                .exchange().expectStatus().isOk();
        CitizenDTO citizenSecond = responseSecond.expectBody(CitizenDTO.class).returnResult().getResponseBody();
        assertEquals(modelMapper.map(ethalonId5ModifiedAsCitizenDomainClass, CitizenDTO.class), citizenSecond);
    }

    private URI getLocationUri(WebTestClient.ResponseSpec response) {
        log.info("Response headers: \'{}\'", response.expectBody().returnResult().getResponseHeaders());

        assertTrue(response.expectBody().returnResult().getResponseHeaders().containsKey("Location"));
        URI location = response.expectBody().returnResult().getResponseHeaders().getLocation();
        assertNotNull(location);
        log.info("Response Location header: \'{}\'", location);
        return location;
    }

    @Test
    public void findCitizensPaginated() {
        testClient.get().uri("/citizens?page=0&size=5")
                .headers(h -> h.add("Authorization", jwtToken))
                .exchange().expectStatus().isOk()
                .expectBody()
                .jsonPath("$.numberOfElements").isEqualTo(5)
                .jsonPath("$.totalPages").isEqualTo(2)
                .jsonPath("$..[?(@.id==5)].dulnumber").isEqualTo(ethalonId5.getDulnumber());
    }


    @Test
    public void findCitizensPaginatedAndFiltrated() {
        testClient.get().uri("/citizens?page=0&size=5&name=Treutel&address=Sachtjen&dul=474098")
                .headers(h -> h.add("Authorization", jwtToken))
                .exchange().expectStatus().isOk()
                .expectBody()
                .jsonPath("$.numberOfElements").isEqualTo(3)
                .jsonPath("$.totalPages").isEqualTo(1)
                .jsonPath("$..[?(@.id==5)].dulnumber").isEqualTo(ethalonId5.getDulnumber());
    }
}