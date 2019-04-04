package ru.sua.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sua.domain.Citizen;

public interface CitizenRepository extends JpaRepository<Citizen, Long> {
    Citizen findByFullName(String fullName);
}
