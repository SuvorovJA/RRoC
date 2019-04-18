package ru.sua.rroc.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.sua.rroc.domain.Citizen;
import ru.sua.rroc.domain.CitizenDTO;
import ru.sua.rroc.domain.CitizenListRequest;
import ru.sua.rroc.domain.Dto;
import ru.sua.rroc.repository.CitizenRepository;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;


/**
 * Данный REST controller не использует маппинга уровня класса, хотя обслуживает только "/citizens".
 * <p>
 * при доступе к любому {@code @Secured} методу:
 * без аутентификации - возврат код 401 Unauthorized;
 * без авторизации с нужной ролью - код 403 Forbidden;
 */
@RestController
@AllArgsConstructor
@Slf4j
public class CitizenController {

    private CitizenRepository repository;
    private CitizenListSpecification specification;

    /**
     * Найти объект по id.
     *
     * @param id искомый идентификатор
     * @return при удаче - код 200 и найденный объект в виде CitizenDTO;
     * при неудаче - код 404 без тела;
     */
    @Dto(CitizenDTO.class)
    @Secured({"ROLE_READONLY", "ROLE_MODIFICATION"})
    @GetMapping("/citizens/{id}")
    public ResponseEntity<Citizen> getCitizenById(@PathVariable("id") long id) {
        Optional<Citizen> optional = repository.findById(id);
        return optional
                .map(citizen -> ResponseEntity.ok().body(citizen))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Cоздать новый объект.
     *
     * @param citizen в теле запроса принимается Citizen
     * @return при удачном создании - код 201 и Location со ссылкой на Read;
     * если объект уже существует - код 409 без тела;
     * при неудачной валидации аргумента - код 400 без тела;
     * при {@code PSQLException: ERROR: duplicate key value violates
     * unique constraint. Key (dulnumber)=(205343909704) already
     * exists.} - код 500;
     */
    //TODO handle 'violates unique constraint' as 400-BadRequest and description for violated field ?
    @Dto(CitizenDTO.class)
    @Secured("ROLE_MODIFICATION")
    @PostMapping("/citizens")
    public ResponseEntity<Citizen> createCitizen(@Valid @RequestBody Citizen citizen) {
        //https://developer.mozilla.org/ru/docs/Web/HTTP/Status
        if (citizen.getId() > 0 && repository.existsById(citizen.getId()))
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // 409 Conflict
        Citizen created = repository.save(citizen);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(created.getId()).toUri();

        return ResponseEntity.created(location).build(); // 201 created
    }

    /**
     * Удаление объекта.
     *
     * @param id удаляемый идентификатор
     * @return при удаче - код 200;
     * если объект не найден - код 404;
     */
    @Secured("ROLE_MODIFICATION")
    @DeleteMapping("/citizens/{id}")
    public ResponseEntity deleteCitizen(@PathVariable("id") long id) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();

        repository.deleteById(id);

        return ResponseEntity.ok().build();
    }

    /**
     * Изменение объекта.
     *
     * @param id      идентификатор существующего объекта
     * @param citizen в теле запроса принимается Citizen
     * @return при удачном изменении - код 201 и Location со ссылкой на Read;
     * если объект не существует - код 404 без тела;
     * при неудачной валидации аргумента - код 400 без тела;
     * при {@code PSQLException} - код 500;
     */
    @Dto(CitizenDTO.class)
    @Secured("ROLE_MODIFICATION")
    @PutMapping("/citizens/{id}")
    public ResponseEntity<Citizen> updateCitizen(@PathVariable("id") long id, @Valid @RequestBody Citizen citizen) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();

        repository.save(citizen);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();

        return ResponseEntity.created(location).build(); // 201 created

    }

    /**
     * Обрабатывется запрос на постраничную выдачу всего списка объектов.
     * С опциональными условиями фильтрации списка.
     * <p>
     * Все условия фильтра могут частично совпадать с искомым значением (без использования wildcards).
     *
     * @param page    номер запрашиваемой страницы, начиная с 0
     * @param size    количество объектов на странице
     * @param name    опциональный. фильтр по имени
     * @param address опциональный. фильтр по адресу
     * @param dul     опциональный. фильтр по номеру документа
     * @return страница с объектами (@see PageCitizenJsonExample.md)
     */
    @Dto(CitizenDTO.class)
    @Secured({"ROLE_READONLY"})
    @GetMapping(value = "/citizens")
    public Page<Citizen> findCitizensPaginated(@RequestParam("page") int page,
                                               @RequestParam("size") int size,
                                               @RequestParam(value = "name", required = false) String name,
                                               @RequestParam(value = "address", required = false) String address,
                                               @RequestParam(value = "dul", required = false) String dul) {
        Pageable pageable = PageRequest.of(page, size);
        log.info("\n   REQUESTED: page={}, size={}, name={}, address={}, dul={}", page, size, name, address, dul);
        CitizenListRequest searchRequest = new CitizenListRequest(name, dul, address);
        return repository.findAll(specification.getFilter(searchRequest), pageable);
    }

}
