package ru.sua.rroc.controller;


import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import ru.sua.rroc.controller.BaseSpecification;
import ru.sua.rroc.domain.Citizen;
import ru.sua.rroc.domain.CitizenListRequest;

import static org.springframework.data.jpa.domain.Specification.where;


/**
 * класс реализует условие фильтрации(для CriteriaQuery) исходя их трёх возможных параметров поиска
 */
@Component
public class CitizenListSpecification extends BaseSpecification<Citizen, CitizenListRequest> {

    @Override
    public Specification<Citizen> getFilter(CitizenListRequest request) {
        return (root, query, cb) -> where(
                fullNameContains(request.getFullName()))
                .or(addressContains(request.getAddress()))
                .or(dulnumberContains(request.getDulnumber()))
                .toPredicate(root, query, cb);
    }

    private Specification<Citizen> fullNameContains(String search) {
        return CitizenAttributeContains("fullName", search);
    }

    private Specification<Citizen> addressContains(String search) {
        return CitizenAttributeContains("address", search);
    }

    private Specification<Citizen> dulnumberContains(String search) {
        return CitizenAttributeContains("dulnumber", search);
    }

    private Specification<Citizen> CitizenAttributeContains(String attribute, String value) {
        return (root, query, cb) -> {
            if (value == null) return null;
            return cb.like(cb.lower(root.get(attribute)), containsLowerCase(value));
        };
    }
}
