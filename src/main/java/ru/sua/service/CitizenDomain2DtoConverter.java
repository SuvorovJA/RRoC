package ru.sua.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.sua.domain.Citizen;
import ru.sua.domain.CitizenDTO;

@Component
public class CitizenDomain2DtoConverter {

    public Citizen getCitizen(CitizenDTO dto) {
        return new Citizen(dto.getId(),
                dto.getFullName(),
                dto.getDob(),
                dto.getAddress(),
                dto.getDulnumber());
    }

    public CitizenDTO getDto(Citizen citizen) {
        return new CitizenDTO(citizen.getId(),
                citizen.getFullName(),
                citizen.getDob(),
                citizen.getAddress(),
                citizen.getDulnumber());
    }

}
