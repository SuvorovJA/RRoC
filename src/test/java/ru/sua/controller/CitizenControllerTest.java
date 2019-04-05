package ru.sua.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import ru.sua.domain.Citizen;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD) // TODO хм. чегото надо сделать с сотоянием базы
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CitizenControllerTest {

    private static final String ethalonId5Json = "{\"id\":5,\"fullName\":\"Mickey-Treutel\",\"dob\":\"1996-03-19\",\"address\":\"90 Bunker Hill Terrace\",\"dulnumber\":\"205343909704\"}";
    private static final String ethalonId5ModifiedJson = "{\"id\":5,\"fullName\":\"Mickey-Treutel\",\"dob\":\"1996-03-19\",\"address\":\"New address\",\"dulnumber\":\"205343909704\"}";
    private static final String ethalonNewJson = "{\"fullName\":\"Mickey-Mouse\",\"dob\":\"1996-06-06\",\"address\":\"Disney\",\"dulnumber\":\"0000000001\"}";
    private static final String ethalonNewIncorrectJson = "{\"fullName\":\"Donald Duck\",\"dulnumber\":\"2\"}";
    private Citizen ethalonId5;
    private Citizen ethalonId5Modified;
    private Citizen ethalonNew;
    private Citizen ethalonNewIncorrect;

    @Autowired
    private WebTestClient testClient;

    @Autowired
    private ObjectMapper mapper;

    @Before
    public void setUp() throws Exception {
        ethalonId5 = mapper.readValue(ethalonId5Json, Citizen.class);
        ethalonId5Modified = mapper.readValue(ethalonId5ModifiedJson, Citizen.class);
        ethalonNew = mapper.readValue(ethalonNewJson, Citizen.class);
        ethalonNewIncorrect = mapper.readValue(ethalonNewIncorrectJson, Citizen.class);
    }

    @Test
    public void aaa_findAll() {
        testClient.get().uri("/citizens")
                .exchange().expectStatus().isOk()
                .expectBodyList(Citizen.class)
                .hasSize(10);
    }

    @Test
    public void getCitizenById() {
        WebTestClient.ResponseSpec response = testClient.get().uri("/citizens/5")
                .exchange().expectStatus().isOk();
        Citizen citizen = response.expectBody(Citizen.class).returnResult().getResponseBody();
        assertEquals(ethalonId5, citizen);
    }

    @Test
    public void zza__createCitizen() {
        WebTestClient.ResponseSpec response = testClient.post().uri("/citizens")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(BodyInserters.fromObject(ethalonNew))
                .exchange().expectStatus().isOk();
        Citizen citizen = response.expectBody(Citizen.class).returnResult().getResponseBody();
        assertNotNull(citizen);
        ethalonNew.setId(citizen.getId());
        assertEquals(ethalonNew, citizen);
    }

    @Test
    public void createIncorrectCitizen() {
        testClient.post().uri("/citizens")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(BodyInserters.fromObject(ethalonNewIncorrect))
                .exchange().expectStatus().isBadRequest();
    }

    @Test
    public void zzz__deleteCitizen() {
        testClient.get().uri("/citizens/5").exchange().expectStatus().isOk();
        testClient.delete().uri("/citizens/5").exchange().expectStatus().isOk();
        testClient.get().uri("/citizens/5").exchange().expectStatus().isNotFound();
    }

    @Test
    public void updateCitizen() {
        // первое чтение
        WebTestClient.ResponseSpec responseFirst = testClient.get().uri("/citizens/5")
                .exchange().expectStatus().isOk();
        Citizen citizenFirst = responseFirst.expectBody(Citizen.class).returnResult().getResponseBody();
        assertEquals(ethalonId5, citizenFirst);
        // модификация
        WebTestClient.ResponseSpec response = testClient.put().uri("/citizens/5")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(BodyInserters.fromObject(ethalonId5Modified))
                .exchange().expectStatus().isOk();
        Citizen citizen = response.expectBody(Citizen.class).returnResult().getResponseBody();
        assertEquals(ethalonId5Modified, citizen);
        // повторное чтение
        WebTestClient.ResponseSpec responseSecond = testClient.get().uri("/citizens/5")
                .exchange().expectStatus().isOk();
        Citizen citizenSecond = responseSecond.expectBody(Citizen.class).returnResult().getResponseBody();
        assertEquals(ethalonId5Modified, citizenSecond);
    }

    @Test
    public void findCitizensPaginated() {
        testClient.get().uri("/citizens?page=0&size=5")
                .exchange().expectStatus().isOk()
                .expectBody()
                .jsonPath("$.numberOfElements").isEqualTo(5)
                .jsonPath("$.totalPages").isEqualTo(2)
                .jsonPath("$..[?(@.id==5)].dulnumber").isEqualTo(ethalonId5.getDulnumber());
    }
}