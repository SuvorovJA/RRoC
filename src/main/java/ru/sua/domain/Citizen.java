package ru.sua.domain;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Data
public class Citizen {

    @Id
    @GeneratedValue
    private long id;

    @Basic
    @Size(max = 100)
    @Pattern(regexp = "^[а-яёА-ЯЁa-zA-Z\\-]+$")
    private String fullName;

    @Temporal(TemporalType.DATE)
    private Date dob;

    @Basic
    @Size(max = 200)
    private String address;

    @Size(min = 6, max = 12)
    @Column(unique = true)
    @Pattern(regexp = "^[0-9]$+")
    private String dulnumber;


}
