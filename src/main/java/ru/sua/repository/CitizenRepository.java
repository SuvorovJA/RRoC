package ru.sua.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import ru.sua.domain.Citizen;

public interface CitizenRepository extends PagingAndSortingRepository<Citizen, Long> {
    Citizen findByFullName(String fullName);
}
