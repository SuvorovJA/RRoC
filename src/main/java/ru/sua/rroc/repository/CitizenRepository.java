package ru.sua.rroc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.sua.rroc.domain.Citizen;

public interface CitizenRepository extends JpaRepository<Citizen, Long>, JpaSpecificationExecutor<Citizen> {
    Citizen findByFullName(String fullName);
}
