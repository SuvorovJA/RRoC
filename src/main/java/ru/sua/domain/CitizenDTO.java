package ru.sua.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CitizenDTO {
    private long id;
    private String fullName;
    private Date dob;
    private String address;
    private String dulnumber;
}
