package ru.sua.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ru.sua.domain.Citizen;
import ru.sua.repository.CitizenRepository;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
public class CitizenController {

    private CitizenRepository repository;

    @GetMapping("/citizens/{id}")
    public ResponseEntity<Citizen> getCitizenById(@PathVariable("id") long id) {
        Optional<Citizen> optional = repository.findById(id);
        return optional
                .map(citizen -> ResponseEntity.ok().body(citizen))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/citizens")
    public ResponseEntity<Citizen> createCitizen(@Valid @RequestBody Citizen citizen) {
        //https://stackoverflow.com/questions/3825990/http-response-code-for-post-when-resource-already-exists
        if (citizen.getId() > 0 && repository.existsById(citizen.getId()))
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        return ResponseEntity.ok().body(repository.save(citizen));
    }

    @DeleteMapping("/citizens/{id}")
    public ResponseEntity deleteCitizen(@PathVariable("id") long id) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();
        repository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/citizens/{id}")
    public ResponseEntity<Citizen> updateCitizen(@PathVariable("id") long id, @Valid @RequestBody Citizen citizen) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();
        return ResponseEntity.ok().body(repository.save(citizen));
    }

    @GetMapping(value = "/citizens", params = {"page", "size"})
    public Page<Citizen> findCitizensPaginated(@RequestParam("page") int page,
                                               @RequestParam("size") int size,
                                               UriComponentsBuilder uriBuilder,
                                               HttpServletResponse response) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAll(pageable);
    }

    @GetMapping("/citizens") // TODO not in TZ - for removing
    public List<Citizen> findAll() {
        return repository.findAll();
    }

}
