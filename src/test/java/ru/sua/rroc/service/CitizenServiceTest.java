package ru.sua.rroc.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.TransactionSystemException;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.sua.rroc.IntegrationTestInitializer;
import ru.sua.rroc.TestPostgresqlContainer;
import ru.sua.rroc.domain.Citizen;

import javax.validation.ConstraintViolationException;
import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.*;

/**
 * данный класс полностью(почти) повторяет CitizenRepositoryTest
 * с поправкой на работу через CitizenService
 */

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = IntegrationTestInitializer.class)
public class CitizenServiceTest {

    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = TestPostgresqlContainer.getInstance();

    private final String stringIncorrectFullnameChars = "A_Z_9";
    private final String string101chars = "DuplicateJsonObjectContextCustomizerFactory-DuplicateJsonObjectContextCustomizer-DuplicateJsonObjectC";
    private final String stringCorrectFullnameChars = "A-Z-Ш";
    private final String stringCorrectDulnumber = "123456";
    private final String stringShortDulnumber = "12345";
    private final String stringLongDulnumber = "1234567890123";
    private final String stringCorrectAddress = "Адрес Street";
    private final Date dateCorrectDate = new Date();
    private final long sampleId = 11L;

    @Autowired
    private CitizenService service;

    @Test
    public void readingFlywyaDataTest() {
        Optional<Citizen> optional = service.findById(1L);
        assertTrue(optional.isPresent());
        Citizen citizen = optional.get();
        assertEquals("Флетчер-Крёйгер", citizen.getFullName());
    }

    @Test
    public void searchByNameFlywyaDataTest() {
        Citizen citizen = service.findByFullName("Jenee-Crooks");
        assertNotNull(citizen);
        assertEquals("22173309533", citizen.getDulnumber());
    }

    @SuppressWarnings("Duplicates")
    @Test
    public void saveNewValidCitizenTest() {
        Citizen citizen = new Citizen(
                sampleId,
                stringCorrectFullnameChars,
                dateCorrectDate,
                stringCorrectAddress,
                stringCorrectDulnumber);
        service.save(citizen);
        Citizen readedCitizen = service.findByFullName(stringCorrectFullnameChars);
        assertNotNull(readedCitizen);
        assertEquals(citizen.getDulnumber(), readedCitizen.getDulnumber());
    }

    @Test
    public void saveNewIvalidCitizen_FullnameFieldTest() {
        Citizen citizen = new Citizen(
                sampleId + 1,
                stringIncorrectFullnameChars,
                dateCorrectDate,
                stringCorrectAddress,
                stringCorrectDulnumber);
        serviceSaveWithAssertion(citizen);
    }

    @Test
    public void saveNewIvalidCitizen_LongFullnameFieldTest() {
        Citizen citizen = new Citizen(
                sampleId + 1,
                string101chars,
                dateCorrectDate,
                stringCorrectAddress,
                stringCorrectDulnumber);
        serviceSaveWithAssertion(citizen);
    }

    @Test
    public void saveNewIvalidCitizen_ShortDulnumberFieldTest() {
        Citizen citizen = new Citizen(
                sampleId + 1,
                stringCorrectFullnameChars,
                dateCorrectDate,
                stringCorrectAddress,
                stringShortDulnumber);
        serviceSaveWithAssertion(citizen);
    }

    @Test
    public void saveNewIvalidCitizen_LongDulnumberFieldTest() {
        Citizen citizen = new Citizen(
                sampleId + 1,
                stringCorrectFullnameChars,
                dateCorrectDate,
                stringCorrectAddress,
                stringLongDulnumber);
        serviceSaveWithAssertion(citizen);
    }

    @Test
    public void saveNewIvalidCitizen_LongAddressFieldTest() {
        Citizen citizen = new Citizen(
                sampleId + 1,
                stringCorrectFullnameChars,
                dateCorrectDate,
                string101chars + string101chars,
                stringLongDulnumber);
        serviceSaveWithAssertion(citizen);
    }

    private void serviceSaveWithAssertion(Citizen citizen) {
        try {
            service.save(citizen);
        } catch (TransactionSystemException e) {
            assertExpectedNestedException(e);
        }
    }

    private void assertExpectedNestedException(TransactionSystemException e) {
        log.info(e.getCause().getCause().getClass().toString());
        if (!(e.getCause().getCause() instanceof ConstraintViolationException)) {
            Assert.fail("unexpected exception");
        }
    }
}