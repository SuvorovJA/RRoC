package ru.sua.rroc.controller;

import org.springframework.data.jpa.domain.Specification;

public abstract class BaseSpecification<T, U> {

    public abstract Specification<T> getFilter(U request);

    String containsLowerCase(String search) {
        return "%" + search.toLowerCase() + "%";
    }
}
