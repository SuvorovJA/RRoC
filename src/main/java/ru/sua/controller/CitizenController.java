package ru.sua.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ru.sua.domain.Citizen;
import ru.sua.repository.CitizenRepository;
import ru.sua.specification.CitizenListRequest;
import ru.sua.specification.CitizenListSpecification;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@AllArgsConstructor
@Slf4j
public class CitizenController {

    private CitizenRepository repository;
    private CitizenListSpecification specification;

    @Secured({"ROLE_READONLY", "ROLE_MODIFICATION"})
    @GetMapping("/citizens/{id}")
    public ResponseEntity<Citizen> getCitizenById(@PathVariable("id") long id) {
        Optional<Citizen> optional = repository.findById(id);
        return optional
                .map(citizen -> ResponseEntity.ok().body(citizen))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Secured("ROLE_MODIFICATION")
    @PostMapping("/citizens")
    public ResponseEntity<Citizen> createCitizen(@Valid @RequestBody Citizen citizen) {
        //https://stackoverflow.com/questions/3825990/http-response-code-for-post-when-resource-already-exists
        if (citizen.getId() > 0 && repository.existsById(citizen.getId()))
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        return ResponseEntity.ok().body(repository.save(citizen));
    }

    @Secured("ROLE_MODIFICATION")
    @DeleteMapping("/citizens/{id}")
    public ResponseEntity deleteCitizen(@PathVariable("id") long id) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();
        repository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @Secured("ROLE_MODIFICATION")
    @PutMapping("/citizens/{id}")
    public ResponseEntity<Citizen> updateCitizen(@PathVariable("id") long id, @Valid @RequestBody Citizen citizen) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();
        return ResponseEntity.ok().body(repository.save(citizen));
    }

    @Secured({"ROLE_READONLY"})
    @GetMapping(value = "/citizens") //,  params = {"page", "size", "name", "address", "dul"})
    public Page<Citizen> findCitizensPaginated(@RequestParam("page") int page,
                                               @RequestParam("size") int size,
                                               @RequestParam(value = "name", required = false) String name,
                                               @RequestParam(value = "address", required = false) String address,
                                               @RequestParam(value = "dul", required = false) String dul,
                                               UriComponentsBuilder uriBuilder,
                                               HttpServletResponse response) {
        Pageable pageable = PageRequest.of(page, size);
        log.info("\n   >>>>  page={}, size={}, name={}, address={}, dul={}", page, size, name, address, dul);
        CitizenListRequest searchRequest = new CitizenListRequest(name, dul, address);
        return repository.findAll(specification.getFilter(searchRequest), pageable);
    }

}
