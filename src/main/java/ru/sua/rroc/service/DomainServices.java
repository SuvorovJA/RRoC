package ru.sua.rroc.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

public interface DomainServices<T, ID extends Long, PG extends Page<T>, S extends Specification<T>, PE extends Pageable> {
    Optional<T> findById(ID id);

    T findByFullName(String name);

    boolean existsById(ID id);

    void deleteById(ID id);

    T save(T entity);

    PG findAll(S specification, PE pageable);
}