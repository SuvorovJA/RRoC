package ru.sua.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.sua.domain.Citizen;
import ru.sua.repository.CitizenRepository;

import java.util.Optional;

/**
 * данный класс символизирует собой бизнес-логику приложения
 *
 * на текущий момент является просто адаптером к CitizenRepository
 *
 */

@Service
@AllArgsConstructor
public class CitizenService implements DomainServices<Citizen, Long, Page<Citizen>, Specification<Citizen>, Pageable> {

    private CitizenRepository repository;

    @Override
    public Optional<Citizen> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Citizen findByFullName(String name) {
        return repository.findByFullName(name);
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Citizen save(Citizen entity) {
        return repository.save(entity);
    }

    @Override
    public Page<Citizen> findAll(Specification<Citizen> specification, Pageable pageable) {
        return repository.findAll(specification, pageable);
    }
}
